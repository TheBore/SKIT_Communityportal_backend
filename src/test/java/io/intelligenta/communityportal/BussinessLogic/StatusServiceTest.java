package io.intelligenta.communityportal.BussinessLogic;

import io.intelligenta.communityportal.models.*;
import io.intelligenta.communityportal.models.exceptions.StatusCanNotBeDeletedException;
import io.intelligenta.communityportal.models.exceptions.StatusNotFoundException;
import io.intelligenta.communityportal.repository.StatusRepository;
import io.intelligenta.communityportal.service.StatusService;
import io.intelligenta.communityportal.service.impl.StatusServiceImpl;
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
public class StatusServiceTest {
    @Mock
    private StatusRepository statusRepository;

    private StatusService statusService;
    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
        StatusType statusType = StatusType.НАП;
        Status status = new Status();
        status.setStatusMk("Статус мк");
        status.setIsEvaluable(false);
        status.setStatusType(statusType);
        status.setId(1L);
        StatusType statusType1 = StatusType.МЕРКА;
        Status status1 = new Status();
        status1.setStatusMk("Статус ал");
        status1.setIsEvaluable(false);
        status1.setStatusType(statusType1);
        status1.setId(2L);
        Status status2 = new Status();
        status2.setStatusMk("Статус мк");
        status2.setIsEvaluable(false);
        status2.setStatusType(statusType);
        status2.setId(3L);
        List<Activity> activities = new ArrayList<>();
        List<Measure> measures = new ArrayList<>();
        List<NAP> naps = new ArrayList<>();
        List<Indicator> indicators = new ArrayList<>();
        status2.setActivities(activities);
        status2.setMeasures(measures);
        status2.setIndicators(indicators);
        List<Status> dataList = new ArrayList<>();
        List<Status> dataListNap = new ArrayList<>();
        NAP nap = new NAP();
        nap.setId(1L);
        nap.setStatus(status);
        nap.setNameMk("НАП мк");
        nap.setDescriptionMk("НАП опис");
        naps.add(nap);
        status2.setNaps(naps);
        dataList.add(status);
        dataList.add(status1);
        dataList.add(status2);
        dataListNap.add(status);
        dataListNap.add(status2);
        Mockito.when(this.statusRepository.save(Mockito.any(Status.class))).thenReturn(status);
        Mockito.when(this.statusRepository.findById((Long)5L)).thenThrow(StatusNotFoundException.class);
        Mockito.when(this.statusRepository.findById((Long)1L)).thenReturn(Optional.of(status));
        Mockito.when(this.statusRepository.findById((Long)2L)).thenReturn(Optional.of(status1));
        Mockito.when(this.statusRepository.findById((Long)3L)).thenReturn(Optional.of(status2));
        Mockito.when(this.statusRepository.findAll()).thenReturn(dataList);
        Mockito.when(this.statusRepository.findAllByKeyword("нап")).thenReturn(dataListNap);
        this.statusService = Mockito.spy(new StatusServiceImpl(this.statusRepository));
    }

    @Test
    public void testStatusCreated(){
        StatusType statusType = StatusType.НАП;
        Status status = new Status();
        status.setStatusMk("Статус мк");
        status.setIsEvaluable(false);
        status.setStatusType(statusType);
        Status statusToBeSaved = this.statusService.createStatus(status);
        Mockito.verify(this.statusService).createStatus(status);
        Assert.assertNotNull("Status is null", statusToBeSaved);
        Assert.assertEquals("Status Type matches", statusToBeSaved.getStatusType(), statusType);
    }

    @Test
    public void testGetStatusById(){
        Status statusReturned = this.statusService.findStatusById(1L);
        Mockito.verify(this.statusService).findStatusById(statusReturned.getId());
        Status statusReturned2 = this.statusService.findStatusById(2L);
        Mockito.verify(this.statusService).findStatusById(statusReturned.getId());
        Assert.assertNotNull(statusReturned);
        Assert.assertNotNull(statusReturned2);
    }

    @Test(expected = StatusNotFoundException.class)
    public void testGetStatusByNonexistentId(){
        Status statusReturned = this.statusService.findStatusById(5L);
        Mockito.verify(this.statusService).findStatusById(statusReturned.getId());
    }

    @Test
    public void testGetAllStatus(){
        List<Status> listStatus = this.statusService.findAll();
        Mockito.verify(this.statusService).findAll();
        Assert.assertNotNull("The list returned is null", listStatus);
        Assert.assertEquals(listStatus.size(), 3);
    }

    @Test
    public void testWithKeyword(){
        List<Status> napStatusList = this.statusService.findAllWithKeyword("НАП");
        Mockito.verify(this.statusService).findAllWithKeyword("НАП");
        Assert.assertNotNull("The list returned is null", napStatusList);
        Assert.assertEquals(napStatusList.size(), 2);
    }

    @Test
    public void testWithNonexistentKeyword(){
        List<Status> napStatusList = this.statusService.findAllWithKeyword("ИЗВЕШТАЈ");
        Mockito.verify(this.statusService).findAllWithKeyword("ИЗВЕШТАЈ");
        Assert.assertNotNull("The list returned is null", napStatusList);
        Assert.assertEquals(napStatusList.size(), 0);
    }

    @Test
    public void testWithEmptyKeyword(){
        List<Status> napStatusList = this.statusService.findAllWithKeyword("");
        Mockito.verify(this.statusService).findAllWithKeyword("");
        Assert.assertNotNull("The list returned is null", napStatusList);
        Assert.assertEquals(napStatusList.size(), 3);
    }

    @Test
    public void testUpdateStatus(){
        Status statusToUpdate = this.statusService.findStatusById(1L);
        statusToUpdate.setStatusAl("Статус ал");
        statusToUpdate.setStatusEn("Status en");
        Status statusUpdated = this.statusService.updateStatus(statusToUpdate);
        Mockito.verify(this.statusService).updateStatus(statusToUpdate);
        Assert.assertNotNull("The status is null", statusUpdated);
        Assert.assertEquals(statusUpdated.getStatusAl(), "Статус ал");
        Assert.assertEquals(statusUpdated.getStatusEn(), "Status en");
        Assert.assertNotEquals(statusUpdated.getStatusEn(), null);
    }

    @Test(expected = StatusNotFoundException.class)
    public void testUpdateNonexistentStatus(){
        Status statusUpdated = this.statusService.updateStatus(this.statusService.findStatusById(5L));
        Mockito.verify(this.statusService).updateStatus(statusUpdated);
    }

    @Test(expected = StatusCanNotBeDeletedException.class)
    public void testDeleteStatusById(){
        this.statusService.deleteStatusById(3L);
        Mockito.verify(statusService).deleteStatusById(3L);
    }

}



