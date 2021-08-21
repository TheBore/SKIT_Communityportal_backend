package io.intelligenta.communityportal.repository;

import io.intelligenta.communityportal.models.ActivityInstitution;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ActivityInstitutionRepository extends JpaSpecificationRepository<ActivityInstitution>,
        PagingAndSortingRepository<ActivityInstitution, Long> {

    @Query("select a from ActivityInstitution a order by a.dateCreated desc")
    Page<ActivityInstitution> findAllWithoutKeyword(Pageable pageable);

    @Query("select a from ActivityInstitution a where (a.nameMk like %:keyword% or LOWER(a.nameMk) like %:keyword% or a.nameEn like %:keyword% or LOWER(a.nameEn) like %:keyword% or a.nameAl like %:keyword% or LOWER(a.nameAl) like %:keyword% or a.institution.nameMk like %:keyword% or LOWER(a.institution.nameMk) like %:keyword% or a.institution.nameAl like %:keyword% or LOWER(a.institution.nameAl) like %:keyword% or a.institution.nameEn like %:keyword% or LOWER(a.institution.nameEn) like %:keyword%) order by a.dateCreated desc")
    Page<ActivityInstitution> findAllWithKeyword(String keyword, Pageable pageable);
}
