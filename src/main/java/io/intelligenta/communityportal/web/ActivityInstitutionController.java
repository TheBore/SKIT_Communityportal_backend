package io.intelligenta.communityportal.web;

import io.intelligenta.communityportal.models.Activity;
import io.intelligenta.communityportal.models.ActivityInstitution;
import io.intelligenta.communityportal.models.dto.ActivityInstitutionDto;
import io.intelligenta.communityportal.service.ActivityInstitutionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/activityInstitution")
public class ActivityInstitutionController {

    private final ActivityInstitutionService activityInstitutionService;

    public ActivityInstitutionController(ActivityInstitutionService activityInstitutionService) {
        this.activityInstitutionService = activityInstitutionService;
    }

    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    @GetMapping("/paged")
    public Page<ActivityInstitution> findAllPaged(@RequestParam("keyword") String keyword, Pageable pageable){
        return activityInstitutionService.findAllPaged(keyword, pageable);
    }

    @GetMapping("/all-listed")
    public List<ActivityInstitution> findAllListed(){
        return activityInstitutionService.findAll();
    }

    @GetMapping("/{id}")
    public ActivityInstitution findById(@PathVariable Long id){
        return activityInstitutionService.findById(id);
    }

    @PostMapping("/create")
    public ActivityInstitution createActivityInstitution(@RequestBody ActivityInstitutionDto activityInstitution){
        return activityInstitutionService.create(activityInstitution);
    }

    @PutMapping("/update")
    public ActivityInstitution updateActivityInstitution(@RequestBody ActivityInstitutionDto activityInstitution){
        return activityInstitutionService.updateInst(activityInstitution);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteById(@PathVariable Long id){
        activityInstitutionService.deleteById(id);
    }

}
