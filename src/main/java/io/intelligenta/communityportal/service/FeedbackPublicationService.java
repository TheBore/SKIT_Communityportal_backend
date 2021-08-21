package io.intelligenta.communityportal.service;

import io.intelligenta.communityportal.models.dto.FeedbackPubDto;
import io.intelligenta.communityportal.models.feedback.FeedbackPublication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * @author Zeljko Nikoloski (nikoloskizeljko@gmail.com)
 */
public interface FeedbackPublicationService {

    void publishFeedbackToInstitution(Long feedbackId, List<Long> insIds);

    void publishFeedbackToTags(Long feedbackId, List<Long> tagIds);

    void publishFeedbackToCategories(Long feedbackId, List<Long> categoryIds);

    void deleteFeedbackPublication(Long feedbackPubId);

    FeedbackPublication markFeedbackPublicationRead(Long feedbackPubId);

    void reminderFeedback(Long fpinstitutionId, String message);

    void escalateFeedback(String direktorEmail, String message);

    FeedbackPublication getFeedbackPubById(Long id);

    FeedbackPublication submitAnswers(Long id, Map<Long, String> feedbackItemIdToAnswer);

    Page<FeedbackPubDto> findByInstitutionIdOrderByDateCreatedDesc(Long institutionId, LocalDate date, Pageable pageable);
}
