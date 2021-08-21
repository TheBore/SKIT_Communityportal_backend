package io.intelligenta.communityportal.repository;

import io.intelligenta.communityportal.models.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ActivityRepository extends JpaSpecificationRepository<Activity>,
        PagingAndSortingRepository<Activity, Long> {
    @Query("select a from Activity a order by a.dateCreated desc nulls last")
    Page<Activity> findAllOrderByDateCreated(Pageable pageable);

}
