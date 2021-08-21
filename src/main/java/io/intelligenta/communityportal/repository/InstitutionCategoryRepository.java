package io.intelligenta.communityportal.repository;

import io.intelligenta.communityportal.models.InstitutionCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.lang.annotation.Native;
import java.math.BigInteger;
import java.util.List;


@Repository
public interface InstitutionCategoryRepository extends JpaSpecificationRepository<InstitutionCategory> {

    @Query("SELECT i FROM InstitutionCategory i ORDER BY i.orderId ASC")
    Page<InstitutionCategory> findAll(Pageable pageable);

    @Query("SELECT i FROM InstitutionCategory i ORDER BY i.orderId ASC")
    List<InstitutionCategory> findAll();

    @Query("SELECT i FROM InstitutionCategory i WHERE i.nameMk  LIKE %:keyword% OR LOWER(i.nameMk) LIKE %:keyword% OR i.nameAl LIKE %:keyword% OR LOWER(i.nameAl) LIKE %:keyword% OR i.nameEn LIKE %:keyword% OR LOWER(i.nameEn) LIKE %:keyword% ORDER BY i.orderId ASC")
    Page<InstitutionCategory> search (@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT i FROM InstitutionCategory i WHERE i.nameMk  LIKE %:keyword% OR LOWER(i.nameMk) LIKE %:keyword% OR i.nameAl LIKE %:keyword% OR LOWER(i.nameAl) LIKE %:keyword% OR i.nameEn LIKE %:keyword% OR LOWER(i.nameEn) LIKE %:keyword% ORDER BY i.orderId ASC")
    List<InstitutionCategory> searchSize(@Param("keyword") String keyword);

    @Query(value = "select * from categories_recursive_function(:id)", nativeQuery = true)
    List<BigInteger> getAllByCategoryRecursively(@Param("id") Long id);

    InstitutionCategory findByOrderId(Long orderId);
}

