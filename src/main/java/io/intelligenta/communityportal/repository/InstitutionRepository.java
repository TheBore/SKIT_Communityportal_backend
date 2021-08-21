package io.intelligenta.communityportal.repository;

import io.intelligenta.communityportal.models.Institution;
import io.intelligenta.communityportal.models.InstitutionCategory;
import io.intelligenta.communityportal.models.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

/**
 * @author Zeljko Nikoloski (nikoloskizeljko@gmail.com)
 */
@Repository
public interface InstitutionRepository extends JpaSpecificationRepository<Institution>, PagingAndSortingRepository<Institution, Long> {

    List<Institution> findAllByTags(Tag tag);

    List<Institution> findAllByCategory(InstitutionCategory category);

    @Query(value = "select i from Institution i where i.active=true and  (i.nameAl like %:keyword% or LOWER(i.nameAl) like %:keyword% or i.nameMk like %:keyword% or LOWER(i.nameMk) like %:keyword% or i.nameEn like %:keyword% or LOWER(i.nameEn) like %:keyword% or i.direktorEmail like  %:keyword% or LOWER(i.direktorEmail) like %:keyword% or i.direktorFirstName like %:keyword% or LOWER(i.direktorFirstName) like %:keyword% or i.direktorLastName like %:keyword% or LOWER(i.direktorLastName) like %:keyword% or i.streetMk like %:keyword% or LOWER(i.streetMk) like %:keyword% or i.streetAl like %:keyword% or LOWER(i.streetAl) like %:keyword% or i.streetEn like %:keyword% or LOWER(i.streetEn) like %:keyword% or i.cityMk like  %:keyword% or LOWER(i.cityMk) like %:keyword% or i.cityAl like  %:keyword% or LOWER(i.cityAl) like %:keyword% or i.cityEn like  %:keyword% or LOWER(i.cityEn) like %:keyword% or i.webSite like  %:keyword% or LOWER(i.webSite) like %:keyword%) ORDER BY i.nameMk, i.nameAl, i.nameEn ASC ")
    Page<Institution> findWithFilter(@Param("keyword") String keyword, Pageable pageable);

    @Query(value = "select i from Institution i where i.active=true and  (i.nameAl like %:keyword% or LOWER(i.nameAl) like %:keyword% or i.nameMk like %:keyword% or LOWER(i.nameMk) like %:keyword% or i.nameEn like %:keyword% or LOWER(i.nameEn) like %:keyword% or i.direktorEmail like  %:keyword% or LOWER(i.direktorEmail) like %:keyword% or i.direktorFirstName like %:keyword% or LOWER(i.direktorFirstName) like %:keyword% or i.direktorLastName like %:keyword% or LOWER(i.direktorLastName) like %:keyword% or i.streetMk like %:keyword% or LOWER(i.streetMk) like %:keyword% or i.streetAl like %:keyword% or LOWER(i.streetAl) like %:keyword% or i.streetEn like %:keyword% or LOWER(i.streetEn) like %:keyword% or i.cityMk like  %:keyword% or LOWER(i.cityMk) like %:keyword% or i.cityAl like  %:keyword% or LOWER(i.cityAl) like %:keyword% or i.cityEn like  %:keyword% or LOWER(i.cityEn) like %:keyword% or i.webSite like  %:keyword% or LOWER(i.webSite) like %:keyword%) ORDER BY i.nameMk, i.nameAl, i.nameEn ASC ")
    List<Institution> findFilter(@Param("keyword") String keyword);

    Page<Institution> findAllByActiveAndEdited(Boolean active, Boolean edited, Pageable pageable);

    @Query(value = "select * from institutions_recursive_function(:id)", nativeQuery = true)
    List<BigInteger> getAllByParentInstitutionRecursively(@Param("id") Long id);
}
