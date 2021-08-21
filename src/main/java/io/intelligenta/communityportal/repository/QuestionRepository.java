package io.intelligenta.communityportal.repository;

import io.intelligenta.communityportal.models.AreaOfInterest;
import io.intelligenta.communityportal.models.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepository extends JpaSpecificationRepository<Question>, PagingAndSortingRepository<Question, Long> {

    Page<Question> findAllByActiveOrderByDateUpdatedDesc(Boolean active, Pageable pageable);

    Page<Question> findAllByActiveAndAreaOfInterestIdOrderByDateUpdatedDesc(Boolean active, Long areaOfInterestId, Pageable pageable);

    Page<Question> findAllByActiveAndAuthorId(Boolean active, Long authorId, Pageable pageable);

    @Query("SELECT q FROM Question q ORDER BY q.dateUpdated Desc ")
    Page<Question> findAllOrderByDateUpdatedDesc(Pageable pageable);

    @Query("select q from Question q where q.areaOfInterest in :areasOfInterest")
    Page<Question> getAllByUserAreasOfInterest (@Param(value = "areasOfInterest")List<AreaOfInterest> areasOfInterest, Pageable pageable);
}
