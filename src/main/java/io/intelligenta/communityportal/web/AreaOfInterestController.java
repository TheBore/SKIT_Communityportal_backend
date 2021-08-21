package io.intelligenta.communityportal.web;

import io.intelligenta.communityportal.models.AreaOfInterest;
import io.intelligenta.communityportal.models.dto.AreaOfInterestDto;
import io.intelligenta.communityportal.service.AreaOfInterestService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/rest/area-of-interest")
public class AreaOfInterestController {

    private final AreaOfInterestService areaOfInterestService;

    public AreaOfInterestController (AreaOfInterestService areaOfInterestService){
        this.areaOfInterestService=areaOfInterestService;
    }

    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN') || hasRole('ROLE_INSTITUTIONAL_MODERATOR')")
    @PostMapping("/create")
    public AreaOfInterest createAreaOfInterest (@RequestBody AreaOfInterestDto areaOfInterestDto){
        return areaOfInterestService.createAreaOfInterest(areaOfInterestDto);
    }

    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN') || hasRole('ROLE_INSTITUTIONAL_MODERATOR')")
    @GetMapping("/{id}")
    public AreaOfInterest findAreaOfInterestById(@PathVariable(value = "id") Long id){
        return areaOfInterestService.findById(id);
    }

    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN') || hasRole('ROLE_INSTITUTIONAL_MODERATOR')")
    @GetMapping("/get-all")
    public Page<AreaOfInterest> findAllAreaOfInterests (@RequestParam("keyword") String keyword, Pageable pageable){
        return areaOfInterestService.findAllAreasOfInterest(keyword, pageable);
    }

    @GetMapping("/allActive")
    public List<AreaOfInterest> findAllActive(){
        return areaOfInterestService.findAll();
    }

    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN') || hasRole('ROLE_INSTITUTIONAL_MODERATOR')")
    @PutMapping("/update")
    public AreaOfInterest updateAreaOfInterest(@RequestBody AreaOfInterestDto areaOfInterestDto){
        return areaOfInterestService.updateAreaOfInterest(areaOfInterestDto);
    }

    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN') || hasRole('ROLE_INSTITUTIONAL_MODERATOR')")
    @PutMapping("/delete/{id}")
    public AreaOfInterest deleteAreaOfInterest (@PathVariable Long id ){
        return areaOfInterestService.setInactive(id);
    }

    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN') || hasRole('ROLE_INSTITUTIONAL_MODERATOR')")
    @PutMapping("/undelete/{id}")
    public AreaOfInterest undeleteAreaOfInterest (@PathVariable Long id){
        return areaOfInterestService.setActive(id);
    }


    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN') || hasRole('ROLE_INSTITUTIONAL_MODERATOR')")
    @GetMapping("/areasForUser")
    public List<AreaOfInterest> findAllAreasForUser(){
        return areaOfInterestService.findAreasOfInterestForUser();
    }


}
