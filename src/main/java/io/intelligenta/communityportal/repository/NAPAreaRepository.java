package io.intelligenta.communityportal.repository;

import io.intelligenta.communityportal.models.NAPArea;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface NAPAreaRepository extends JpaSpecificationRepository<NAPArea>, PagingAndSortingRepository<NAPArea, Long> {

    @Query("select napArea from NAPArea napArea where napArea.active=true and (napArea.nameMk like %:keyword% or LOWER(napArea.nameMk) like %:keyword% or  napArea.nameEn like %:keyword% or LOWER(napArea.nameEn) like %:keyword% or napArea.nameAl like %:keyword% or LOWER(napArea.nameAl) like %:keyword% or napArea.descriptionMk like %:keyword% or LOWER(napArea.descriptionMk) like %:keyword% or napArea.descriptionEn like %:keyword% or LOWER(napArea.descriptionEn) like %:keyword% or napArea.descriptionAl like %:keyword% or LOWER(napArea.descriptionAl) like %:keyword%)")
    Page<NAPArea> findAllByActiveWithKeyword(String keyword, Pageable pageable);

    @Query("select napArea from NAPArea napArea where napArea.active=true")
    Page<NAPArea> findAllByActiveWithoutKeyword(Pageable pageable);

}
