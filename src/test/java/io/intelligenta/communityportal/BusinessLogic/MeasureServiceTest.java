package io.intelligenta.communityportal.BusinessLogic;

import io.intelligenta.communityportal.models.*;
import io.intelligenta.communityportal.models.dto.MeasureDto;
import io.intelligenta.communityportal.models.exceptions.MeasureNotFoundException;
import io.intelligenta.communityportal.models.exceptions.MeasureWithoutProblemException;
import io.intelligenta.communityportal.repository.MeasureRepository;
import io.intelligenta.communityportal.repository.NAPRepository;
import io.intelligenta.communityportal.repository.ProblemRepository;
import io.intelligenta.communityportal.repository.StatusRepository;
import io.intelligenta.communityportal.repository.auth.UserRepository;
import io.intelligenta.communityportal.service.MeasureService;
import io.intelligenta.communityportal.service.impl.MeasureServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class MeasureServiceTest {
    @Mock
    private  MeasureRepository measureRepository;
    @Mock
    private  UserRepository userRepository;
    @Mock
    private  NAPRepository napRepository;
    @Mock
    private  StatusRepository statusRepository;
    @Mock
    private  ProblemRepository problemRepository;

    MeasureService measureService;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
        Measure measure = new Measure();
        measure.setId(1L);
        measure.setActive(true);
        measure.setNameMk("Мерка за подобрување");
        Measure measure1 = new Measure();
        measure1.setId(2L);
        measure1.setActive(false);
        measure1.setNameMk("Мерка за подобрување");
        List<Measure> measures = new ArrayList<>();
        measures.add(measure);
        measures.add(measure1);
        StatusType statusType = StatusType.НАП;
        Status status = new Status();
        status.setStatusMk("Статус мк");
        status.setIsEvaluable(false);
        status.setStatusType(statusType);
        status.setId(1L);
        Problem problem = new Problem();
        problem.setId(1L);
        Problem problem1 = new Problem();
        problem.setId(2L);
        measure.setProblem(problem);
        measure1.setProblem(problem1);
        Mockito.when(this.measureRepository.save(Mockito.any(Measure.class))).thenReturn(measure);
        Mockito.when(this.measureRepository.findById(1L)).thenReturn(Optional.of(measure));
        Mockito.when(this.measureRepository.findById(2L)).thenReturn(Optional.of(measure1));
        Mockito.when(this.problemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(problem));
        Mockito.when(this.statusRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(status));
        this.measureService = Mockito.spy(new MeasureServiceImpl(this.measureRepository,
                this.userRepository,
                this.napRepository,
                this.statusRepository,
                this.problemRepository));
    }

    @Test
    public void testCreateMeasure(){
        MeasureDto measureDto = new MeasureDto();
        measureDto.setActive(true);
        measureDto.setNameMk("Мерка мк");
        measureDto.setNameAl("");
        measureDto.setNameEn("");
        measureDto.setDescriptionMk("");
        measureDto.setDescriptionAl("");
        measureDto.setDescriptionEn("");
        measureDto.setStartDate("12/12/2021");
        measureDto.setEndDate("12/12/2022");
        measureDto.setStatus(1L);
        measureDto.setProblem(1L);
        Measure measure = this.measureService.createMeasure(measureDto);
        Mockito.verify(this.measureService).createMeasure(measureDto);
        Assert.assertNotNull(measure);
    }

    @Test(expected = MeasureWithoutProblemException.class)
    public void testCreateMeasureWithoutProblem(){
        MeasureDto measureDto = new MeasureDto();
        measureDto.setActive(true);
        measureDto.setNameMk("Мерка мк");
        measureDto.setNameAl("");
        measureDto.setNameEn("");
        measureDto.setDescriptionMk("");
        measureDto.setDescriptionAl("");
        measureDto.setDescriptionEn("");
        measureDto.setStartDate("12/12/2021");
        measureDto.setEndDate("12/12/2022");
        measureDto.setStatus(1L);
        Measure measure = this.measureService.createMeasure(measureDto);
        Mockito.verify(this.measureService).createMeasure(measureDto);
    }

    @Test
    public void testFindMeasureById()
    {
        Measure measure = this.measureService.findMeasureById(1L);
        Mockito.verify(this.measureService).findMeasureById(1L);
        Assert.assertNotNull(measure);
        Assert.assertTrue(measure.getId() == 1L);
    }

    @Test(expected = MeasureNotFoundException.class)
    public void testFindMeasureByNonexistentId()
    {
        Measure measure = this.measureService.findMeasureById(100L);
        Mockito.verify(this.measureService).findMeasureById(100L);
    }

    @Test
    public void testUpdateMeasure(){
        MeasureDto measureDto = new MeasureDto();
        measureDto.setActive(true);
        measureDto.setNameMk("Мерка мк");
        measureDto.setNameAl("");
        measureDto.setNameEn("");
        measureDto.setDescriptionMk("");
        measureDto.setDescriptionAl("");
        measureDto.setDescriptionEn("");
        measureDto.setStartDate("12/12/2021");
        measureDto.setEndDate("12/12/2022");
        measureDto.setStatus(1L);
        measureDto.setId(1L);
        Measure measure = this.measureService.updateMeasure(measureDto);
        Mockito.verify(this.measureService).updateMeasure(measureDto);
        Assert.assertNotNull(measure);
        Measure measureUpdated = this.measureService.findMeasureById(1L);
        Assert.assertEquals(measureUpdated.getNameMk(), "Мерка мк");
    }

    @Test
    public void testSetInactiveMeasure(){
        Measure measure = this.measureService.setInactive(1L);
        Mockito.verify(this.measureService).setInactive(1L);
        Assert.assertNotNull(measure);
        Assert.assertFalse(measure.getActive());
    }

    @Test
    public void testSetActiveMeasure(){
        Measure measure = this.measureService.setActive(2L);
        Mockito.verify(this.measureService).setActive(2L);
        Assert.assertNotNull(measure);
        Assert.assertTrue(measure.getActive());
    }

}
