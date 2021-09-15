package io.intelligenta.communityportal.web;

import io.intelligenta.communityportal.models.Activity;
import io.intelligenta.communityportal.models.ActivityInstitution;
import io.intelligenta.communityportal.models.dto.ActivityDto;
import io.intelligenta.communityportal.service.ActivityService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/activity")
public class ActivityController {

    private final ActivityService activityService;

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

//    @PreAuthorize("isAuthenticated()")
    @GetMapping("/all")
    public Page<Activity> findAll(Pageable pageable){
        return activityService.findAllActivities(pageable);
    }

//    @PreAuthorize("isAuthenticated()")
    @GetMapping("/all-list")
    public List<Activity> findAllList(){
        return activityService.findAllActivitiesList();
    }

//    @PreAuthorize("isAuthenticated()")
    @GetMapping("/all-by-measure/{id}")
    public List<Activity> findAllActivitiesByMeasureId(@PathVariable Long id){
        return activityService.findAllActivitiesByMeasureId(id);
    }

//    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public Activity findById(@PathVariable Long id){
        return activityService.findById(id);
    }

//    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    @PostMapping("/create")
    public Activity createActivity(@RequestBody ActivityDto activity){
        return activityService.createActivity(activity);
    }

//    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    @PostMapping("/update/{id}")
    public Activity updateActivity(@RequestBody ActivityDto activity,@PathVariable Long id){
        return activityService.updateActivity(activity, id);
    }

//    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    @PostMapping("/delete/{id}")
    public Activity deleteActivity(@PathVariable Long id){
        return activityService.setInactive(id);
    }

//    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    @PostMapping("/undelete/{id}")
    public Activity undeleteActivity(@PathVariable Long id){
        return activityService.setActive(id);
    }

//    @PreAuthorize("isAuthenticated()")
    @GetMapping("/all-institutions-by-activity/{id}")
    public List<ActivityInstitution> allInstitutionsByActivity(@PathVariable Long id){
        return activityService.allInstitutionsByActivity(id);
    }

//    @PreAuthorize("isAuthenticated()")
    @GetMapping("/all-by-institution/{id}")
    public List<Activity> allActivitiesByInstitution(@PathVariable Long id){
        return activityService.allActivitiesByInstitution(id);
    }

}