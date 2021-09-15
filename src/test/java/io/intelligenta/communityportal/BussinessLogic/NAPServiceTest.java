package io.intelligenta.communityportal.BussinessLogic;

import io.intelligenta.communityportal.models.*;
import io.intelligenta.communityportal.models.auth.User;
import io.intelligenta.communityportal.models.auth.UserRole;
import io.intelligenta.communityportal.models.dto.NAPDto;
import io.intelligenta.communityportal.models.exceptions.NAPNotFoundException;
import io.intelligenta.communityportal.repository.EvaluationRepository;
import io.intelligenta.communityportal.repository.IndicatorReportRepository;
import io.intelligenta.communityportal.repository.NAPRepository;
import io.intelligenta.communityportal.repository.StatusRepository;
import io.intelligenta.communityportal.repository.auth.UserRepository;
import io.intelligenta.communityportal.service.NAPService;
import io.intelligenta.communityportal.service.impl.NAPServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Printable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class NAPServiceTest {
    @Mock
    private  NAPRepository napRepository;
    @Mock
    private  UserRepository userRepository;
    @Mock
    private  StatusRepository statusRepository;
    @Mock
    private  EvaluationRepository evaluationRepository;
    @Mock
    private  IndicatorReportRepository indicatorReportRepository;

    NAPService napService;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
        StatusType statusType = StatusType.НАП;
        Status status = new Status();
        status.setStatusMk("Во тек");
        status.setIsEvaluable(false);
        status.setStatusType(statusType);
        status.setId(4L);
        StatusType statusType1 = StatusType.ИЗВЕШТАЈ;
        Status status1 = new Status();
        status1.setStatusMk("Завршен");
        status1.setIsEvaluable(false);
        status1.setStatusType(statusType1);
        status1.setId(1L);
        NAP nap = new NAP();
        nap.setId(1L);
        nap.setStatus(status);
        nap.setNameMk("НАП мк");
        nap.setDescriptionMk("НАП опис");
        nap.setActive(true);
        nap.setStartDate(LocalDate.now());
        nap.setOpenForEvaluation(true);
        NAP nap1 = new NAP();
        nap1.setId(2L);
        nap1.setStatus(status);
        nap1.setNameMk("НАП мк");
        nap1.setDescriptionMk("НАП опис");
        nap1.setActive(true);
        nap1.setStartDate(LocalDate.now().minusDays(3));
        NAP nap2 = new NAP();
        nap2.setId(3L);
        nap2.setStatus(status);
        nap2.setNameMk("НАП мк");
        nap2.setDescriptionMk("НАП опис");
        nap2.setActive(false);
        nap2.setStartDate(LocalDate.now().minusDays(5));
        User user = new User();
        user.setEmail("admin@gmail.com");
        user.setFirstName("Hi");
        user.setRole(UserRole.ROLE_ADMIN);
        userRepository.save(user);
        nap.setCreatedByUser(user);
        napRepository.save(nap);
        List<NAP> napListDescByDateAndActive = new ArrayList<>();
        napListDescByDateAndActive.add(nap);
        napListDescByDateAndActive.add(nap1);
        List<NAP> napList = new ArrayList<>();
        napList.add(nap);
        napList.add(nap1);
        napList.add(nap2);
        Evaluation evaluation = new Evaluation();
        evaluation.setId(1L);
        evaluation.setOpen(true);
        IndicatorReport indicatorReport = new IndicatorReport();
        indicatorReport.setId(1L);
        indicatorReport.setEvaluation(evaluation);
        Mockito.when(evaluationRepository.findByNapAndOpen((Mockito.any(NAP.class)), Mockito.anyBoolean())).thenReturn(evaluation);
        Mockito.when(evaluationRepository.save(Mockito.any(Evaluation.class))).thenReturn(evaluation);
        Mockito.when(napRepository.findAllByOrderByDateCreated()).thenReturn(napListDescByDateAndActive);
        Mockito.when(napRepository.save(Mockito.any(NAP.class))).thenReturn(nap);
        Mockito.when(napRepository.findById(1L)).thenReturn(Optional.of(nap));
        Mockito.when(napRepository.findById(3L)).thenReturn(Optional.of(nap2));
        Mockito.when(statusRepository.save(Mockito.any(Status.class))).thenReturn(status);
        Mockito.when(statusRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(status));
        this.napService = Mockito.spy(new NAPServiceImpl(this.napRepository, userRepository, statusRepository, evaluationRepository, indicatorReportRepository));
    }

    @Test
    public void createNAP(){
        StatusType statusType = StatusType.НАП;
        Status status = new Status();
        status.setStatusMk("Статус мк");
        status.setIsEvaluable(false);
        status.setStatusType(statusType);
        status.setId(3L);
        Status s = statusRepository.save(status);
        NAPDto napDto = new NAPDto();
        napDto.setNameMk("НАП мк");
        napDto.setDescriptionMk("НАП опис");
        napDto.setStatus(s.getId());
        napDto.setStartDate("11/28/2021");
        napDto.setEndDate("11/28/2024");
        NAP napToBeSaved = this.napService.createNAP(napDto);
        Mockito.verify(this.napService).createNAP(napDto);
        Assert.assertNotNull("Status is null", napToBeSaved);
        Assert.assertEquals("Names don't match", napToBeSaved.getNameMk(), napDto.getNameMk());
    }

    @Test
    public void testGetNAPById(){
        NAP napToBeReturned = this.napService.findById(1L);
        Mockito.verify(this.napService).findById(1L);
        Assert.assertNotNull("NAP id not found", napToBeReturned);
        Assert.assertEquals("NAP mk name isn't correct", napToBeReturned.getNameMk(), "НАП мк");
    }

    @Test(expected = NAPNotFoundException.class)
    public void testGetNAPByNonexistentId(){
        NAP napToBeReturned = this.napService.findById(4L);
        Mockito.verify(this.napService).findById(4L);
    }

    @Test
    public void testAllByOrderByDateCreated(){
        List<NAP> napList = this.napService.findAllByDateCreatedDescAndActiveList();
        Mockito.verify(this.napService).findAllByDateCreatedDescAndActiveList();
        Assert.assertNotNull(napList);
        Assert.assertEquals(napList.size(), 2);
        for(NAP nap : napList){
            Assert.assertEquals(nap.getActive(), true);
        }
        Assert.assertTrue(napList.get(0).getStartDate().isAfter(napList.get(napList.size()-1).getStartDate()));
    }
    @Test
    public void testChangeEvaluationStatus(){
        napService.changeEvaluationStatus(false, 1L);
        Mockito.verify(napService).changeEvaluationStatus(false, 1L);
    }

    @Test(expected = NAPNotFoundException.class)
    public void testChangeEvaluationStatusNonexistent(){
        napService.changeEvaluationStatus(false, 8L);
        Mockito.verify(napService).changeEvaluationStatus(false, 8L);
    }

    @Test
    public void testUpdateNAP(){
        NAP napToUpdate = this.napService.findById(1L);
        Long id = napToUpdate.getId();
        NAPDto napDto = new NAPDto();
        Status status = new Status();
        status.setId(4L);
        status.setStatusType(StatusType.НАП);
        napDto.setStatus(status.getId());
        napDto.setNameMk("НАП мк");
        napDto.setNameAl("");
        napDto.setNameEn("Nap en");
        napDto.setDescriptionMk("НАП опис");
        napDto.setDescriptionAl("");
        napDto.setDescriptionEn("");
        napDto.setStartDate("11/28/2021");
        napDto.setEndDate("11/28/2024");
        NAP napUpdated = this.napService.updateNAP(napDto, id);
        Mockito.verify(this.napService).updateNAP(napDto, id);
        Assert.assertNotNull(napUpdated);
        Assert.assertEquals(napUpdated.getNameMk(), "НАП мк");
        Assert.assertEquals(napUpdated.getNameEn(), "Nap en");

    }

    @Test
    public void testSetNAPInactive(){
        NAP nap = this.napService.findById(1L);
        NAP napInactive = this.napService.setInactive(nap.getId());
        Mockito.verify(napService).setInactive(nap.getId());
        Assert.assertNotNull(napInactive);
        Assert.assertFalse(napInactive.getActive());
    }

    @Test
    public void testSetNAPActive(){
        NAP nap = this.napService.findById(3L);
        NAP napActive = this.napService.setActive(nap.getId());
        Mockito.verify(napService).setActive(nap.getId());
        Assert.assertNotNull(napActive);
        Assert.assertTrue(napActive.getActive());
    }







}
