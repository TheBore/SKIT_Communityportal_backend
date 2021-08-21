package io.intelligenta.communityportal.repository;

import io.intelligenta.communityportal.models.RequestForEvaluation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface RequestForEvaluationRepository extends JpaSpecificationRepository<RequestForEvaluation>, PagingAndSortingRepository<RequestForEvaluation, Long> {

    @Query(value = "select req from RequestForEvaluation req")
    Page<RequestForEvaluation> findAllPaged(Pageable pageable);
}
