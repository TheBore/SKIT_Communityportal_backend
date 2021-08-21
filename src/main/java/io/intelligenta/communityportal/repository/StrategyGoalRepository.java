package io.intelligenta.communityportal.repository;

import io.intelligenta.communityportal.models.StrategyGoal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface StrategyGoalRepository extends JpaSpecificationRepository<StrategyGoal>, PagingAndSortingRepository<StrategyGoal, Long> {

    @Query("select str from StrategyGoal str where str.active=true")
    Page<StrategyGoal> findAllWithoutKeyword(Pageable pageable);

    @Query("select str from StrategyGoal str where str.active=true and (str.nameMk like %:keyword% or LOWER(str.nameMk) like %:keyword% or str.nameEn like %:keyword% or LOWER(str.nameEn) like %:keyword% or str.nameAl like %:keyword% or LOWER(str.nameAl) like %:keyword% or str.descriptionMk like %:keyword% or LOWER(str.descriptionMk) like %:keyword% or str.descriptionEn like %:keyword% or LOWER(str.descriptionEn) like %:keyword% or str.descriptionAl like %:keyword% or LOWER(str.descriptionAl) like %:keyword%)")
    Page<StrategyGoal> findAllWithKeyword(String keyword, Pageable pageable);

}
