package io.intelligenta.communityportal.service.impl;

import io.intelligenta.communityportal.models.exceptions.FeedbackItemNotFoundException;
import io.intelligenta.communityportal.models.exceptions.FeedbackNotFoundException;
import io.intelligenta.communityportal.models.exceptions.FeedbackPublicationNotFoundException;
import io.intelligenta.communityportal.models.feedback.Feedback;
import io.intelligenta.communityportal.models.feedback.FeedbackItem;
import io.intelligenta.communityportal.models.feedback.FeedbackPublication;
import io.intelligenta.communityportal.repository.FeedbackItemRepository;
import io.intelligenta.communityportal.repository.FeedbackPublicationRepository;
import io.intelligenta.communityportal.repository.FeedbackRepository;
import io.intelligenta.communityportal.service.FeedbackItemService;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Zeljko Nikoloski (nikoloskizeljko@gmail.com)
 */
@Service
public class FeedbackItemServiceImpl implements FeedbackItemService {
    FeedbackItemRepository feedbackItemRepository;
    FeedbackRepository feedbackRepository;
    FeedbackPublicationRepository feedbackPublicationRepository;

    public FeedbackItemServiceImpl(FeedbackItemRepository feedbackItemRepository,
                                   FeedbackRepository feedbackRepository,
                                   FeedbackPublicationRepository feedbackPublicationRepository) {
        this.feedbackItemRepository = feedbackItemRepository;
        this.feedbackRepository = feedbackRepository;
        this.feedbackPublicationRepository = feedbackPublicationRepository;
    }


    @Override
    public FeedbackItem addFeedbackItem(FeedbackItem feedbackItem, Long feedbackId) {
        Feedback feedback = feedbackRepository.findById(feedbackId).orElseThrow(FeedbackNotFoundException::new);
        FeedbackItem fi = new FeedbackItem();
        fi.setName(feedbackItem.getName());
        fi.setDescription(feedbackItem.getDescription());
        fi.setType(feedbackItem.getType());
        fi.setOptions(feedbackItem.getOptions());
        fi.setFeedback(feedback);
        fi.setRequired(feedbackItem.isRequired());
        fi.setDateCreated(LocalDateTime.now());
        fi.setDateUpdated(LocalDateTime.now());
        feedbackItemRepository.save(fi);
        return fi;
    }

    @Override
    public void deleteFeedbackItem(Long feeditemId) {
        FeedbackItem feedbackItem = feedbackItemRepository.findById(feeditemId).orElseThrow(FeedbackItemNotFoundException::new);
        feedbackItemRepository.delete(feedbackItem);
    }

    @Override
    public FeedbackItem updateFeedbackItem(FeedbackItem feedbackItem) {
        FeedbackItem fi = feedbackItemRepository.findById(feedbackItem.getId()).orElseThrow(FeedbackItemNotFoundException::new);
        fi.setName(feedbackItem.getName());
        fi.setDescription(feedbackItem.getDescription());
        fi.setType(feedbackItem.getType());
        fi.setOptions(feedbackItem.getOptions());
        fi.setDateUpdated(LocalDateTime.now());
        fi.setRequired(feedbackItem.isRequired());
        feedbackItemRepository.save(fi);
        return fi;
    }

    @Override
    public List<FeedbackItem> getAllFeedbackItemsByFeedbackPub(FeedbackPublication feedbackPublication) {

        FeedbackPublication feedbackPub = feedbackPublicationRepository.findById(feedbackPublication.getId()).orElseThrow(FeedbackPublicationNotFoundException::new);
        Feedback feedback = feedbackPub.getFeedback();
        Long feedbackId = feedback.getId();

        return feedbackItemRepository.findByFeedbackId(feedbackId);

    }

    public List<FeedbackItem> getAllFeedbackItemByFeedId(Long feedbackId) {
        return feedbackItemRepository.findByFeedbackId(feedbackId);
    }
}
