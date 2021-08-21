package io.intelligenta.communityportal.service;

import io.intelligenta.communityportal.models.IndicatorReport;
import io.intelligenta.communityportal.models.dto.IndicatorReportDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IndicatorReportService {
    Page<IndicatorReport> findAllIndicatorReports(Pageable pageable);

    IndicatorReport findById(Long id);

    IndicatorReport createIndicatorReport(IndicatorReportDto indicatorReport);

    IndicatorReport updateIndicatorReport(IndicatorReportDto indicatorReport, Long activityId, Long evaluationId   );

    IndicatorReport setActive(Long id);
    IndicatorReport setInactive(Long id);

    List<IndicatorReport> findAllIndicatorReportsList();

    List<IndicatorReport> findAllByActivity(Long id);

    Page<IndicatorReport> findAllIndicatorReportsPageByActivityId(Long id, Pageable pageable);
}
