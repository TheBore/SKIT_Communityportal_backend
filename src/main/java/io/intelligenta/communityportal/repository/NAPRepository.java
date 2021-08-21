package io.intelligenta.communityportal.repository;

import io.intelligenta.communityportal.models.NAP;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface NAPRepository extends JpaSpecificationRepository<NAP>, PagingAndSortingRepository<NAP, Long> {

    @Query("SELECT n FROM NAP n ORDER BY n.startDate DESC")
    Page<NAP> findAllOrderByDateCreated(Pageable pageable);

    @Query("SELECT n FROM NAP n where n.active = true ORDER BY n.startDate DESC NULLS LAST")
    List<NAP> findAllByOrderByDateCreated();

}
