package io.intelligenta.communityportal.web;

import io.intelligenta.communityportal.models.Indicator;
import io.intelligenta.communityportal.models.dto.IndicatorDto;
import io.intelligenta.communityportal.service.IndicatorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/indicator")
public class IndicatorController {
    private final IndicatorService indicatorService;

    public IndicatorController(IndicatorService indicatorService) {
        this.indicatorService = indicatorService;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/all")
    public Page<Indicator> findAll(Pageable pageable){
        return indicatorService.findAllIndicators(pageable);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/all-list")
    public List<Indicator> findAll(){
        return indicatorService.findAllIndicatorsList();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/all-by-activity/{id}")
    public List<Indicator> findAllByIndicatorId(@PathVariable Long id){
        return indicatorService.findAllIndicatorsByActivityId(id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/all-by-activity-page/{id}")
    public Page<Indicator> findAllPageByIndicatorId(@PathVariable Long id, Pageable pageable){
        return indicatorService.findAllIndicatorsPageByActivityId(id, pageable);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public Indicator findById(@PathVariable Long id){
        return indicatorService.findById(id);
    }

    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    @PostMapping("/create")
    public Indicator createIndicator(@RequestBody IndicatorDto indicator){
        return indicatorService.createIndicator(indicator);
    }

    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    @PostMapping("/update/{id}")
    public Indicator updateIndicator(@RequestBody IndicatorDto indicator, @PathVariable Long id){
        return indicatorService.updateIndicator(indicator, id);
    }

    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    @PostMapping("/delete/{id}")
    public Indicator deleteIndicator(@PathVariable Long id){
        return indicatorService.setInactive(id);
    }

    @PreAuthorize("isAuthenticated() && hasRole('ROLE_ADMIN')")
    @PostMapping("/undelete/{id}")
    public Indicator undeleteIndicator(@PathVariable Long id){
        return indicatorService.setActive(id);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/update-indicator-on-evaluation")
    public Indicator updateIndicatorOnEvaluation(@RequestBody IndicatorDto indicatorDto){
        return indicatorService.updateIndicatorOnEvaluation(indicatorDto);
    }
}