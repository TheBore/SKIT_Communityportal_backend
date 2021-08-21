package io.intelligenta.communityportal.repository;

import io.intelligenta.communityportal.models.Activity;
import io.intelligenta.communityportal.models.Evaluation;
import io.intelligenta.communityportal.models.Indicator;
import io.intelligenta.communityportal.models.IndicatorReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface IndicatorReportRepository extends JpaSpecificationRepository<IndicatorReport>,
        PagingAndSortingRepository<IndicatorReport, Long> {

    @Query("select i from IndicatorReport i order by i.dateCreated desc nulls last")
    Page<IndicatorReport>  findIndicatorReportsByActiveOrderByDateUpdated(Pageable pageable);

    Page<IndicatorReport> findAllByActivity(Activity activity,Pageable pageable);

    IndicatorReport findByActivityAndEvaluation(Activity activity, Evaluation evaluation);

    List<IndicatorReport> findAllByEvaluation(Evaluation evaluation);

}
