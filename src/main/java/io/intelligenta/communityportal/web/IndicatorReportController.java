package io.intelligenta.communityportal.web;

import io.intelligenta.communityportal.models.Indicator;
import io.intelligenta.communityportal.models.IndicatorReport;
import io.intelligenta.communityportal.models.dto.IndicatorReportDto;
import io.intelligenta.communityportal.service.IndicatorReportService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/indicator-report")
public class IndicatorReportController {
    private final IndicatorReportService indicatorReportService;

    public IndicatorReportController(IndicatorReportService indicatorReportService) {
        this.indicatorReportService = indicatorReportService;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/all")
    public Page<IndicatorReport> findAll(Pageable pageable){
        return indicatorReportService.findAllIndicatorReports(pageable);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/all-list")
    public List<IndicatorReport> findAllList(){
        return indicatorReportService.findAllIndicatorReportsList();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public IndicatorReport findById(@PathVariable Long id){
        return indicatorReportService.findById(id);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public IndicatorReport createIndicatorReport(@RequestBody IndicatorReportDto IndicatorReport){
        return indicatorReportService.createIndicatorReport(IndicatorReport);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/update/{activityId}/{evaluationId}")
    public IndicatorReport updateIndicatorReport(@RequestBody IndicatorReportDto IndicatorReport, @PathVariable Long activityId, @PathVariable Long evaluationId){
        return indicatorReportService.updateIndicatorReport(IndicatorReport, activityId, evaluationId);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/delete/{id}")
    public IndicatorReport deleteIndicatorReport(@PathVariable Long id){
        return indicatorReportService.setInactive(id);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/undelete/{id}")
    public IndicatorReport undeleteIndicatorReport(@PathVariable Long id){
        return indicatorReportService.setActive(id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/all-by-activity/{id}")
    public List<IndicatorReport> findAllByActivity(@PathVariable Long id){
        return indicatorReportService.findAllByActivity(id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/all-by-activity-page/{id}")
    public Page<IndicatorReport> findAllPageByIndicatorId(@PathVariable Long id, Pageable pageable){
        return indicatorReportService.findAllIndicatorReportsPageByActivityId(id, pageable);
    }
}