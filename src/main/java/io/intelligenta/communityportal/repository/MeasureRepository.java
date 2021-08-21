package io.intelligenta.communityportal.repository;

import io.intelligenta.communityportal.models.Measure;
import io.intelligenta.communityportal.models.NAP;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface MeasureRepository extends JpaSpecificationRepository<Measure>, PagingAndSortingRepository<Measure, Long> {

    @Query("SELECT m FROM Measure m ORDER BY m.dateCreated DESC")
    Page<Measure> findAllOrderByDateCreatedDesc(Pageable pageable);

   /* List<Measure> findAllByNapId(Long napId);*/

}
