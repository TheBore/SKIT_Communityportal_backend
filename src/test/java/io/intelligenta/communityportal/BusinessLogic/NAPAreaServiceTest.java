package io.intelligenta.communityportal.BusinessLogic;

import io.intelligenta.communityportal.models.NAPArea;
import io.intelligenta.communityportal.models.NAPAreaType;
import io.intelligenta.communityportal.models.Problem;
import io.intelligenta.communityportal.models.dto.NapAreaDto;
import io.intelligenta.communityportal.models.exceptions.NAPAreaNotFoundException;
import io.intelligenta.communityportal.models.exceptions.NAPAreaTypeNotFoundException;
import io.intelligenta.communityportal.models.exceptions.NapAreaCanNotBeDeletedException;
import io.intelligenta.communityportal.repository.NAPAreaRepository;
import io.intelligenta.communityportal.repository.NAPAreaTypeRepository;
import io.intelligenta.communityportal.repository.auth.UserRepository;
import io.intelligenta.communityportal.service.NAPAreaService;
import io.intelligenta.communityportal.service.impl.NAPAreaServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class NAPAreaServiceTest {
    @Mock
    private  NAPAreaRepository napAreaRepository;
    @Mock
    private  UserRepository userRepository;
    @Mock
    private  NAPAreaTypeRepository napAreaTypeRepository;

    NAPAreaService napAreaService;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
        NAPArea napArea = new NAPArea();
        napArea.setId(1L);
        napArea.setActive(false);
        NAPArea napArea1 = new NAPArea();
        napArea1.setId(2L);
        napArea1.setActive(true);
        List<Problem> problemList = new ArrayList<>();
        napArea1.setProblems(problemList);
        NAPAreaType napAreaType = new NAPAreaType();
        napAreaType.setId(1L);
        List<NAPArea> napAreaList = new ArrayList<>();
        napAreaList.add(napArea);
        napAreaList.add(napArea1);
        Mockito.when(this.napAreaRepository.findAll()).thenReturn(napAreaList);
        Mockito.when(this.napAreaTypeRepository.findById(1L)).thenReturn(Optional.of(napAreaType));
        Mockito.when(this.napAreaRepository.findById(1L)).thenReturn(Optional.of(napArea));
        Mockito.when(this.napAreaRepository.save(napArea)).thenReturn(napArea);
        Mockito.when(this.napAreaRepository.findById(2L)).thenReturn(Optional.of(napArea1));
        Mockito.when(this.napAreaRepository.save(napArea1)).thenReturn(napArea1);
        this.napAreaService = Mockito.spy(new NAPAreaServiceImpl(this.napAreaRepository, this.userRepository, this.napAreaTypeRepository));
    }

    @Test
    public void testFindById(){
        NAPArea napArea = this.napAreaService.findById(1L);
        Mockito.verify(this.napAreaService).findById(1L);
        Assert.assertNotNull(napArea);
        Assert.assertTrue(napArea.getId() == 1L);
    }

    @Test(expected = NAPAreaNotFoundException.class)
    public void testFindByNonexistentId(){
        NAPArea napArea = this.napAreaService.findById(100L);
        Mockito.verify(this.napAreaService).findById(100L);
        Assert.assertNotNull(napArea);
    }

    @Test
    public void testCreateNAPArea(){
        NapAreaDto napAreaDto = new NapAreaDto();
        napAreaDto.setId(1L);
        napAreaDto.setNameMk("НАП област мк");
        napAreaDto.setNameAl("");
        napAreaDto.setNameEn("");
        napAreaDto.setDescriptionAl("");
        napAreaDto.setDescriptionEn("");
        napAreaDto.setDescriptionMk("");
        napAreaDto.setCode("Code");
        napAreaDto.setNapAreaTypeId(100L);
        Assertions.assertThrows(NAPAreaTypeNotFoundException.class,
                () -> this.napAreaService.createNAPArea(napAreaDto));
        Mockito.verify(this.napAreaService).createNAPArea(napAreaDto);
        napAreaDto.setNapAreaTypeId(1L);
        NAPArea napAreaMock = new NAPArea();
        napAreaMock.setId(1L);
        Mockito.when(this.napAreaRepository.save(Mockito.any(NAPArea.class))).thenReturn(napAreaMock);
        NAPArea napArea = this.napAreaService.createNAPArea(napAreaDto);
        Assert.assertNotNull(napArea);
    }

    @Test
    public void testUpdateNAPArea(){
        NapAreaDto napAreaDto = new NapAreaDto();
        Assertions.assertThrows(NAPAreaNotFoundException.class,
                () -> this.napAreaService.updateNAPArea(napAreaDto));
        Mockito.verify(this.napAreaService).updateNAPArea(napAreaDto);
        napAreaDto.setId(1L);
        napAreaDto.setNameMk("НАП област мк");
        napAreaDto.setNameAl("");
        napAreaDto.setNameEn("NAP area en");
        napAreaDto.setDescriptionAl("");
        napAreaDto.setDescriptionEn("");
        napAreaDto.setDescriptionMk("");
        napAreaDto.setCode("Code");
        napAreaDto.setNapAreaTypeId(1L);
        NAPArea napArea = this.napAreaService.updateNAPArea(napAreaDto);
        Assert.assertNotNull(napArea);
        Assert.assertEquals(napArea.getNameEn(), "NAP area en");
    }

    @Test
    public void testSetNAPAreaActive(){
        NAPArea napArea = this.napAreaService.findById(1L);
        Assert.assertFalse(napArea.getActive());
        Assertions.assertThrows(NAPAreaNotFoundException.class,
                () -> this.napAreaService.setActive(100L));
        Mockito.verify(this.napAreaService).setActive(100L);
        napArea = this.napAreaService.setActive(1L);
        Assert.assertNotNull(napArea);
        Assert.assertTrue(napArea.getActive());
    }

    @Test
    public void testSetNAPAreaInactive(){
        NAPArea napArea = this.napAreaService.findById(2L);
        Assert.assertTrue(napArea.getActive());
        Assertions.assertThrows(NAPAreaNotFoundException.class,
                () -> this.napAreaService.setInactive(200L));
        Mockito.verify(this.napAreaService).setInactive(200L);
        napArea = this.napAreaService.setInactive(2L);
        Assert.assertNotNull(napArea);
        Assert.assertFalse(napArea.getActive());
        Problem problem = new Problem();
        problem.setId(1L);
        List<Problem> problemList = new ArrayList<>();
        problemList.add(problem);
        napArea.setProblems(problemList);
        Assertions.assertThrows(NapAreaCanNotBeDeletedException.class,
                () -> this.napAreaService.setInactive(2L));
    }

    @Test
    public void testFindAllNAPAreas(){
        List<NAPArea> napAreas = this.napAreaService.findAllList();
        Mockito.verify(this.napAreaService).findAllList();
        Assert.assertNotNull(napAreas);
        Assert.assertEquals(napAreas.size(), 1);
        Assert.assertTrue(napAreas.get(0).getActive());
    }
}
