package io.intelligenta.communityportal.repository;

import io.intelligenta.communityportal.models.AreaOfInterest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AreaOfInterestRepository extends JpaSpecificationRepository<AreaOfInterest>, PagingAndSortingRepository<AreaOfInterest, Long> {

    @Query("select a from AreaOfInterest a where a.active=true order by a.dateCreated desc nulls last ")
    Page<AreaOfInterest> findAreaOfInterestOrderByDateCreated(Pageable pageable);

    @Query("select a from AreaOfInterest a where a.active=true and (a.nameMk  LIKE %:keyword% OR LOWER(a.nameMk) LIKE %:keyword% OR a.nameAl  LIKE %:keyword% OR LOWER(a.nameAl) LIKE %:keyword% OR a.nameEn  LIKE %:keyword% OR LOWER(a.nameEn) LIKE %:keyword%) order by a.dateCreated desc nulls last ")
    Page<AreaOfInterest> findAreaOfInterestOrderByDateCreatedWithKeyword(String keyword, Pageable pageable);

    @Query("select a from AreaOfInterest a where a.nameMk in :names ")
    List<AreaOfInterest> getAreasOfInterestByName (@Param("names") List<String> names);
}
