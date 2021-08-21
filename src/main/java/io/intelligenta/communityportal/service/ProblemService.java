package io.intelligenta.communityportal.service;

import io.intelligenta.communityportal.models.Problem;
import io.intelligenta.communityportal.models.dto.ProblemDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProblemService {

    Problem createProblem (ProblemDto problem);

    Problem findById (Long id);

    Page<Problem> findAllPaged (Pageable pageable);

    List<Problem> findAllList ();

    Problem updateProblem (ProblemDto problem);

    Problem setInactive (Long id);

    Problem setActive (Long id);

    List<Problem> findAllByNapAreaId(Long id);

}
