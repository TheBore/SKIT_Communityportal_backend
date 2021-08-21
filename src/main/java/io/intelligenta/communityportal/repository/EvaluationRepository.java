package io.intelligenta.communityportal.repository;

import io.intelligenta.communityportal.models.Evaluation;
import io.intelligenta.communityportal.models.NAP;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface EvaluationRepository extends JpaSpecificationRepository<Evaluation>,
        PagingAndSortingRepository<Evaluation, Long> {

    Evaluation findByNapAndOpen(NAP nap, Boolean open);

    List<Evaluation> findAllByNap(NAP nap);
}