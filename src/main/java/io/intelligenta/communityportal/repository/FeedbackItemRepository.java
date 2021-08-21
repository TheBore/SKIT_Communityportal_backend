package io.intelligenta.communityportal.repository;

import io.intelligenta.communityportal.models.feedback.FeedbackItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author Zeljko Nikoloski (nikoloskizeljko@gmail.com)
 */
public interface FeedbackItemRepository extends PagingAndSortingRepository<FeedbackItem, Long> {
    Page<FeedbackItem> findByFeedbackIdOrderByDateCreatedDesc(Long id, Pageable pageable);

    List<FeedbackItem> findByFeedbackId(Long id);

    Optional<FeedbackItem> findById(Long id);
}