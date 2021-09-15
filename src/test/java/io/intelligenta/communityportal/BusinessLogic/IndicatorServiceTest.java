package io.intelligenta.communityportal.BusinessLogic;

import io.intelligenta.communityportal.models.*;
import io.intelligenta.communityportal.models.dto.IndicatorDto;
import io.intelligenta.communityportal.models.exceptions.ActivityNotFoundException;
import io.intelligenta.communityportal.models.exceptions.IndicatorNotFoundException;
import io.intelligenta.communityportal.models.exceptions.InstitutionNotFoundException;
import io.intelligenta.communityportal.models.exceptions.StatusNotFoundException;
import io.intelligenta.communityportal.repository.*;
import io.intelligenta.communityportal.repository.auth.UserRepository;
import io.intelligenta.communityportal.service.IndicatorService;
import io.intelligenta.communityportal.service.impl.IndicatorServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import static org.junit.jupiter.api.Assertions.assertThrows;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class IndicatorServiceTest {
    @Mock
    private  IndicatorRepository indicatorRepository;
    @Mock
    private  UserRepository userRepository;
    @Mock
    private  StatusRepository statusRepository;
    @Mock
    private  ActivityRepository activityRepository;
    @Mock
    private  InstitutionRepository institutionRepository;
    @Mock
    private  ActivityInstitutionRepository activityInstitutionRepository;

    IndicatorService indicatorService;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
        Indicator indicator = new Indicator();
        Indicator indicator1 = new Indicator();
        indicator.setId(1L);
        indicator.setActive(false);
        indicator.setCounter(5);
        indicator1.setId(2L);
        indicator1.setActive(true);
        Activity activity = new Activity();
        activity.setId(1L);
        activity.setActive(true);
        activity.setNameMk("активност мк");
        StatusType statusType = StatusType.НАП;
        Status status = new Status();
        status.setStatusMk("Статус мк");
        status.setIsEvaluable(false);
        status.setStatusType(statusType);
        status.setId(1L);
        ActivityInstitution activityInstitution = new ActivityInstitution();
        activityInstitution.setId(1L);
        List<Indicator> indicatorList = new ArrayList<>();
        indicatorList.add(indicator);
        indicatorList.add(indicator1);
        Mockito.when(this.indicatorRepository.findAll()).thenReturn(indicatorList);
        Mockito.when(this.activityInstitutionRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(activityInstitution));
        Mockito.when(this.activityRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(activity));
        Mockito.when(this.statusRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(status));
        Mockito.when(this.indicatorRepository.findById(1L)).thenReturn(Optional.of(indicator));
        Mockito.when(this.indicatorRepository.findById(2L)).thenReturn(Optional.of(indicator1));
        Mockito.when(this.indicatorRepository.save(indicator)).thenReturn(indicator);
        Mockito.when(this.indicatorRepository.save(indicator1)).thenReturn(indicator1);

        this.indicatorService = Mockito.spy(new IndicatorServiceImpl(
                this.indicatorRepository,
                this.userRepository,
                this.statusRepository,
                this.activityRepository,
                this.institutionRepository,
                this.activityInstitutionRepository
        ));
    }

    @Test
    public void testFindById(){
        Indicator indicator = this.indicatorService.findById(1L);
        Mockito.verify(this.indicatorService).findById(1L);
        Assert.assertNotNull(indicator);
        Assert.assertTrue(indicator.getId() == 1L);
    }

    @Test(expected = IndicatorNotFoundException.class)
    public void testFindByNonexistentId(){
        Indicator indicator = this.indicatorService.findById(100L);
        Mockito.verify(this.indicatorService).findById(indicator.getId());
    }

    @Test
    public void testCreateIndicator(){
        IndicatorDto indicatorDto = new IndicatorDto();
        indicatorDto.setCounter(5);
        indicatorDto.setType("NUMBER");
        indicatorDto.setNameMk("Индикатор");
        indicatorDto.setNameAl("");
        indicatorDto.setNameEn("");
        indicatorDto.setStartDate("12/12/2021");
        indicatorDto.setEndDate("12/12/2022");
        Assertions.assertThrows(StatusNotFoundException.class,() -> this.indicatorService.createIndicator(indicatorDto));
        indicatorDto.setStatus(1L);
        Mockito.verify(this.indicatorService).createIndicator(indicatorDto);
        Assertions.assertThrows(ActivityNotFoundException.class,() -> this.indicatorService.createIndicator(indicatorDto));
        indicatorDto.setActivity(1L);
        Assertions.assertThrows(InstitutionNotFoundException.class,() -> this.indicatorService.createIndicator(indicatorDto));
        indicatorDto.setInstitution(1L);
        Indicator indicatorNew = new Indicator();
        indicatorNew.setId(3L);
        Mockito.when(this.indicatorRepository.save(Mockito.any(Indicator.class))).thenReturn(indicatorNew);
        Indicator indicator = this.indicatorService.createIndicator(indicatorDto);
        Assert.assertNotNull(indicator);
    }

    @Test
    public void testUpdateIndicator(){
        IndicatorDto indicatorDto = new IndicatorDto();
        indicatorDto.setCounter(5);
        indicatorDto.setType("NUMBER");
        indicatorDto.setNameMk("Индикатор");
        indicatorDto.setNameAl("");
        indicatorDto.setNameEn("Indicator");
        indicatorDto.setStartDate("12/12/2021");
        indicatorDto.setEndDate("12/12/2022");
        Assertions.assertThrows(IndicatorNotFoundException.class,() -> this.indicatorService.updateIndicator(indicatorDto, 100L));
        Assertions.assertThrows(StatusNotFoundException.class,() -> this.indicatorService.updateIndicator(indicatorDto, 1L));
        indicatorDto.setStatus(1L);
        Mockito.verify(this.indicatorService).updateIndicator(indicatorDto, 1L);
        Assertions.assertThrows(ActivityNotFoundException.class,() -> this.indicatorService.updateIndicator(indicatorDto, 1L));
        indicatorDto.setActivity(1L);
        Indicator indicatorUpdated = this.indicatorService.updateIndicator(indicatorDto, 1L);
        Assert.assertNotNull(indicatorUpdated);
        Assert.assertEquals(indicatorUpdated.getNameEn(), "Indicator");
        Assert.assertTrue(indicatorUpdated.getId() == 1L);
    }

    @Test
    public void testSetActiveIndicator(){
        Indicator indicator = this.indicatorService.setActive(1L);
        Mockito.verify(this.indicatorService).setActive(indicator.getId());
        Assert.assertNotNull(indicator);
        Assert.assertTrue(indicator.getActive());
    }

    @Test
    public void testSetInactiveIndicator(){
        Indicator indicator1 = this.indicatorService.setInactive(2L);
        Mockito.verify(this.indicatorService).setInactive(indicator1.getId());
        Assert.assertNotNull(indicator1);
        Assert.assertFalse(indicator1.getActive());
    }

    @Test
    public void testFindAllIndicatorsList(){
        List<Indicator> indicators = this.indicatorService.findAllIndicatorsList();
        Mockito.verify(this.indicatorService).findAllIndicatorsList();
        Assert.assertNotNull(indicators);
        Assert.assertEquals(indicators.size(), 2);
    }

    @Test
    public void testUpdateIndicatorOnEvaluation(){
        IndicatorDto indicatorDto = new IndicatorDto();
        indicatorDto.setId(1L);
        indicatorDto.setCounter(5);
        indicatorDto.setType("NUMBER");
        indicatorDto.setNameMk("Индикатор");
        indicatorDto.setNameAl("");
        indicatorDto.setNameEn("Indicator");
        indicatorDto.setStartDate("12/12/2021");
        indicatorDto.setEndDate("12/12/2022");
        indicatorDto.setStatus(1L);
        indicatorDto.setActivity(1L);
        indicatorDto.setCounter(5);
        Indicator indicator = this.indicatorService.updateIndicatorOnEvaluation(indicatorDto);
        Mockito.verify(this.indicatorService).updateIndicatorOnEvaluation(indicatorDto);
        Assert.assertNotNull(indicator);
        Assert.assertTrue(indicator.getCounter() == indicatorDto.getCounter() + 5);
    }
}
