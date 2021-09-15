package io.intelligenta.communityportal.BussinessLogic;

import io.intelligenta.communityportal.models.*;
import io.intelligenta.communityportal.models.dto.ActivityDto;
import io.intelligenta.communityportal.models.exceptions.ActivityNotFoundException;
import io.intelligenta.communityportal.models.exceptions.StatusNotFoundException;
import io.intelligenta.communityportal.repository.*;
import io.intelligenta.communityportal.repository.Mail.EmailRepository;
import io.intelligenta.communityportal.repository.auth.UserRepository;
import io.intelligenta.communityportal.service.ActivityService;
import io.intelligenta.communityportal.service.impl.ActivityServiceImpl;
import io.intelligenta.communityportal.service.impl.StatusServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class ActivityServiceTest {
    @Mock
    private  ActivityRepository activityRepository;
    @Mock
    private  UserRepository userRepository;
    @Mock
    private  MeasureRepository measureRepository;
    @Mock
    private  StatusRepository statusRepository;
    @Mock
    private  ActivityInstitutionRepository activityInstitutionRepository;
    @Mock
    private  IndicatorReportRepository indicatorReportRepository;
    @Mock
    private InstitutionRepository institutionRepository;
    @Mock
    private  EmailRepository emailRepository;
    @Mock
    private  Environment environment;
    @Mock
    private EvaluationRepository evaluationRepository;

    ActivityService activityService;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
        Activity activity = new Activity();
        Activity activity1 = new Activity();
        activity.setId(1L);
        activity.setActive(true);
        activity.setNameMk("активност мк");
        activity1.setId(2L);
        activity1.setActive(false);
        Measure measure = new Measure();
        measure.setId(1L);
        measure.setNameMk("Мерка за подобрување");
        Measure measure1 = new Measure();
        measure1.setId(2L);
        measure1.setNameMk("Мерка за подобрување");
        activity.setMeasure(measure);
        activity1.setMeasure(measure1);
        StatusType statusType = StatusType.НАП;
        Status status = new Status();
        status.setStatusMk("Статус мк");
        status.setIsEvaluable(false);
        status.setStatusType(statusType);
        status.setId(1L);
        ActivityInstitution activityInstitution = new ActivityInstitution();
        activityInstitution.setId(1L);
        ActivityInstitution activityInstitution2 = new ActivityInstitution();
        activityInstitution2.setId(2L);
        List<ActivityInstitution> activityInstitutionList = new ArrayList<>();
        activityInstitutionList.add(activityInstitution);
        activityInstitutionList.add(activityInstitution2);
        activity.setActivityInstitutions(activityInstitutionList);
        List<Activity> activities = new ArrayList<>();
        activities.add(activity);
        activities.add(activity1);
        Mockito.when(this.activityRepository.findAll()).thenReturn(activities);
        Mockito.when(this.activityInstitutionRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(activityInstitution));
        Mockito.when(this.statusRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(status));
        Mockito.when(this.measureRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(measure));
        Mockito.when(this.activityRepository.save(Mockito.any(Activity.class))).thenReturn(activity);
        Mockito.when(this.activityRepository.findById(1L)).thenReturn(Optional.of(activity));
        Mockito.when(this.activityRepository.findById(2L)).thenReturn(Optional.of(activity1));
        this.activityService = Mockito.spy(new ActivityServiceImpl(
                this.activityRepository,
                this.userRepository,
                this.measureRepository,
                this.statusRepository,
                this.institutionRepository,
                this.activityInstitutionRepository,
                this.evaluationRepository,
                this.indicatorReportRepository,
                this.emailRepository,
                this.environment));
    }

    @Test
    public void testGetActivityById(){
        Activity activity = this.activityService.findById(1L);
        Mockito.verify(this.activityService).findById(activity.getId());
        Assert.assertNotNull("Activity with this id do not exist", activity);
        Assert.assertEquals(activity.getNameMk(), "активност мк");
        Assert.assertTrue(activity.getActive());
    }

    @Test(expected = ActivityNotFoundException.class)
    public void testGetActivityByNonexistentId(){
        Activity activity = this.activityService.findById(100L);
        Mockito.verify(this.activityService).findById(activity.getId());
    }

    @Test
    public void testCreateActivity(){
        ActivityDto activityDto = new ActivityDto();
        activityDto.setNameMk("Активност мк");
        activityDto.setNameAl("");
        activityDto.setNameEn("Activity en");
        activityDto.setActive(true);
        activityDto.setContinuously(true);
        activityDto.setMeasure(1L);
        activityDto.setStatus(1L);
        ActivityInstitution activityInstitution = new ActivityInstitution();
        activityInstitution.setId(1L);
        ActivityInstitution activityInstitution2 = new ActivityInstitution();
        activityInstitution2.setId(2L);
        activityDto.setCompetentInstitution(1L);
        List<Long> activityInstitutionList = new ArrayList<>();
        activityInstitutionList.add(activityInstitution.getId());
        activityInstitutionList.add(activityInstitution2.getId());
        activityDto.setActivityInstitutions(activityInstitutionList);
        Mockito.when(this.activityInstitutionRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(activityInstitution));
        Activity activity = this.activityService.createActivity(activityDto);
        Mockito.verify(activityService).createActivity(activityDto);
        Assert.assertNotNull(activity);
    }

    @Test
    public void testUpdateActivity(){
        ActivityDto activityDto = new ActivityDto();
        activityDto.setNameMk("Активност мк");
        activityDto.setNameAl("");
        activityDto.setNameEn("Activity en");
        activityDto.setActive(true);
        activityDto.setContinuously(true);
        activityDto.setMeasure(1L);
        activityDto.setStatus(1L);
        ActivityInstitution activityInstitution = new ActivityInstitution();
        activityInstitution.setId(1L);
        ActivityInstitution activityInstitution2 = new ActivityInstitution();
        activityInstitution2.setId(2L);
        activityDto.setCompetentInstitution(1L);
        List<Long> activityInstitutionList = new ArrayList<>();
        activityInstitutionList.add(activityInstitution.getId());
        activityInstitutionList.add(activityInstitution2.getId());
        activityDto.setActivityInstitutions(activityInstitutionList);
        Mockito.when(this.activityInstitutionRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(activityInstitution));
        Activity updatedActivity = this.activityService.updateActivity(activityDto, 1L);
        Mockito.verify(activityService).updateActivity(activityDto, 1L);
        Assert.assertNotNull(updatedActivity);
        Assert.assertEquals(updatedActivity.getNameEn(), "Activity en");
        Assert.assertEquals(updatedActivity.getNameMk(), "Активност мк");
        Assert.assertEquals(updatedActivity.getActivityInstitutions().size(), 2);
        Assert.assertTrue(updatedActivity.getContinuously());
        Assert.assertTrue(updatedActivity.getActive());
    }

    @Test
    public void testSetActivityActive(){
        Activity activity= this.activityService.setActive(2L);
        Mockito.verify(this.activityService).setActive(2L);
        Assert.assertTrue(activity.getActive());
    }

    @Test
    public void testSetActivityInactive(){
        Activity activity= this.activityService.setInactive(1L);
        Mockito.verify(this.activityService).setInactive(1L);
        Assert.assertFalse(activity.getActive());
    }

    @Test
    public void testListInstitutionsByActivity(){
        List<ActivityInstitution> activityInstitutionList = this.activityService.allInstitutionsByActivity(1L);
        Mockito.verify(this.activityService).allInstitutionsByActivity(1L);
        Assert.assertNotNull(activityInstitutionList);
        Assert.assertEquals(activityInstitutionList.size(), 3);
    }

    @Test
    public void testFindAllActivitiesList(){
        List<Activity> activities = this.activityService.findAllActivitiesList();
        Mockito.verify(this.activityService).findAllActivitiesList();
        Assert.assertEquals(activities.size(), 2);
    }




}
