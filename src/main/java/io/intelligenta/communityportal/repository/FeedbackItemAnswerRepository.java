package io.intelligenta.communityportal.repository;

import io.intelligenta.communityportal.models.feedback.FeedbackItemAnswer;

import java.util.List;

public interface FeedbackItemAnswerRepository extends JpaSpecificationRepository<FeedbackItemAnswer> {
    List<FeedbackItemAnswer> findByPublicationId(Long publicationId);

    List<FeedbackItemAnswer> findByItemId(Long feedbackitemid);

    List<FeedbackItemAnswer> findByItemFeedbackId(Long feedbackId);

    List<FeedbackItemAnswer> findAllByItemId(Long feedbackItemId);
}
