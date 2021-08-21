package io.intelligenta.communityportal.service;

import io.intelligenta.communityportal.models.feedback.FeedbackItem;
import io.intelligenta.communityportal.models.feedback.FeedbackPublication;

import java.util.List;
import java.util.Optional;

/**
 * @author Zeljko Nikoloski (nikoloskizeljko@gmail.com)
 */
public interface FeedbackItemService {
    FeedbackItem addFeedbackItem(FeedbackItem feedbackItem, Long feedbackId);

    void deleteFeedbackItem(Long feeditemId);

    FeedbackItem updateFeedbackItem(FeedbackItem feedbackItem);

    List<FeedbackItem> getAllFeedbackItemsByFeedbackPub(FeedbackPublication feedbackPublication);

    List<FeedbackItem> getAllFeedbackItemByFeedId(Long feedbackId);
}
