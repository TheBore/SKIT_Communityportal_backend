package io.intelligenta.communityportal.repository;

import io.intelligenta.communityportal.models.AreaOfInterest;
import io.intelligenta.communityportal.models.feedback.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * @author Zeljko Nikoloski (nikoloskizeljko@gmail.com)
 */
public interface FeedbackRepository extends PagingAndSortingRepository<Feedback,Long> {
    List<Feedback> findAllByOrderByDateCreatedDesc();
    Page<Feedback> findAllByOrderByDateCreatedDesc(Pageable pageable);
    Optional<Feedback> findById(Long id);

    List<Feedback> findAllByCreatorId(Long userId);

    @Query("select f from Feedback f where f.areaOfInterest in :areasOfInterest")
    Page<Feedback> getAllByUserAreasOfInterest (@Param("areasOfInterest") List<AreaOfInterest> areasOfInterest, Pageable pageable);
    
}
