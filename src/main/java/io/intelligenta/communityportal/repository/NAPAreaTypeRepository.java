package io.intelligenta.communityportal.repository;

import io.intelligenta.communityportal.models.NAPAreaType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface NAPAreaTypeRepository extends JpaSpecificationRepository<NAPAreaType>, PagingAndSortingRepository<NAPAreaType, Long>{

    @Query("select napArea from NAPAreaType napArea where napArea.active=true and (napArea.nameMk like %:keyword% or LOWER(napArea.nameMk) like %:keyword% or  napArea.nameEn like %:keyword% or LOWER(napArea.nameEn) like %:keyword% or napArea.nameAl like %:keyword% or LOWER(napArea.nameAl) like %:keyword% or napArea.descriptionMk like %:keyword% or LOWER(napArea.descriptionMk) like %:keyword% or napArea.descriptionEn like %:keyword% or LOWER(napArea.descriptionEn) like %:keyword% or napArea.descriptionAl like %:keyword% or LOWER(napArea.descriptionAl) like %:keyword%)")
    Page<NAPAreaType> findAllByActivePaged(String keyword, Pageable pageable);

    @Query("SELECT napArea from NAPAreaType napArea where napArea.active=true")
    Page<NAPAreaType> findAllByActiveNotPaged(Pageable pageable);


}
