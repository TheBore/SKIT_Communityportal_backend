package io.intelligenta.communityportal.repository;

import io.intelligenta.communityportal.models.NAPArea;
import io.intelligenta.communityportal.models.Problem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ProblemRepository extends PagingAndSortingRepository<Problem, Long> {

    Page<Problem> findAll (Pageable pageable);

    List<Problem> findAll ();

    List<Problem> findAllByNapArea(NAPArea napArea);

}
