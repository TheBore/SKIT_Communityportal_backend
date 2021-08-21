package io.intelligenta.communityportal.repository;

import io.intelligenta.communityportal.models.Indicator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface IndicatorRepository extends JpaSpecificationRepository<Indicator>,
        PagingAndSortingRepository<Indicator, Long> {
    @Query("select i from Indicator i order by i.dateCreated desc nulls last")
    Page<Indicator> findIndicatorsOrderByDateCreated(Pageable pageable);

    Page<Indicator> findIndicatorsByActivityId(Long activityId,Pageable pageable);
}
