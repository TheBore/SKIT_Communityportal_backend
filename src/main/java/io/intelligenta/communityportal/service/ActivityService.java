package io.intelligenta.communityportal.service;

import io.intelligenta.communityportal.models.Activity;
import io.intelligenta.communityportal.models.ActivityInstitution;
import io.intelligenta.communityportal.models.dto.ActivityDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ActivityService extends BaseEntityCrudService<Activity> {

    Page<Activity> findAllActivities(Pageable pageable);

    Activity findById(Long id);

    Activity createActivity(ActivityDto activity);

    Activity updateActivity(ActivityDto activity, Long activityId);

    Activity setActive(Long id);

    Activity setInactive(Long id);

    List<Activity> findAllActivitiesList();

    List<Activity> findAllActivitiesByMeasureId(Long id);

    List<ActivityInstitution> allInstitutionsByActivity(Long id);

    List<Activity> allActivitiesByInstitution(Long id);

    void sendWeeklyMail();

}
