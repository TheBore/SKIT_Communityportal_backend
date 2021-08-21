package io.intelligenta.communityportal.service;

import io.intelligenta.communityportal.models.Institution;
import io.intelligenta.communityportal.models.feedback.FeedbackItemAnswer;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FeedbackItemAnswerService {

    FeedbackItemAnswer getFeedbackItemAnswerById (Long id);

    void saveFeedbackItemAnswer (Long feedbackPublicationId, Long feedbackItemId, String answer);

    List<FeedbackItemAnswer> getByPublicationId(Long publicationId);

    List<Institution> getInstitutionsForAnswer(Long feedbackitemId, String answer);

    FeedbackItemAnswer saveAttachmentForAnswer(Long feedbackPublicationId, Long feedbackItemId, String name, String mimeType, Long size, byte[] content);

    List<FeedbackItemAnswer> findAllByFeedbackItemId(Long feedbackItemId);
}
