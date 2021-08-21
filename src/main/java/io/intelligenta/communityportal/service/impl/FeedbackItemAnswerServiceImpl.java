package io.intelligenta.communityportal.service.impl;

import io.intelligenta.communityportal.models.Attachment;
import io.intelligenta.communityportal.models.Institution;
import io.intelligenta.communityportal.models.exceptions.FeedbackItemAnswerNotFoundException;
import io.intelligenta.communityportal.models.exceptions.FeedbackItemNotFoundException;
import io.intelligenta.communityportal.models.exceptions.FeedbackPublicationNotFoundException;
import io.intelligenta.communityportal.models.feedback.*;
import io.intelligenta.communityportal.repository.*;
import io.intelligenta.communityportal.service.FeedbackItemAnswerService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FeedbackItemAnswerServiceImpl implements FeedbackItemAnswerService {

    private FeedbackItemAnswerRepository feedbackItemAnswerRepository;
    private FeedbackItemRepository feedbackItemRepository;
    private FeedbackPublicationRepository feedbackPublicationRepository;
    private final AttachmentRepository attachmentRepository;
    private FeedbackRepository feedbackRepository;

    public FeedbackItemAnswerServiceImpl(FeedbackItemAnswerRepository feedbackItemAnswerRepository, FeedbackItemRepository feedbackItemRepository, FeedbackPublicationRepository feedbackPublicationRepository, AttachmentRepository attachmentRepository) {
        this.feedbackItemAnswerRepository = feedbackItemAnswerRepository;
        this.feedbackItemRepository = feedbackItemRepository;
        this.feedbackPublicationRepository = feedbackPublicationRepository;
        this.attachmentRepository = attachmentRepository;
    }

    @Override
    public FeedbackItemAnswer getFeedbackItemAnswerById(Long id) {
        return feedbackItemAnswerRepository.findById(id).orElseThrow(FeedbackItemAnswerNotFoundException::new);
    }

    @Override
    public void saveFeedbackItemAnswer(Long feedbackPublicationId, Long feedbackItemId, String answer) {

        FeedbackItem feedbackItem = feedbackItemRepository.findById(feedbackItemId).orElseThrow(FeedbackItemNotFoundException::new);

        FeedbackPublication feedbackPublication = feedbackPublicationRepository.findById(feedbackPublicationId).orElseThrow(FeedbackPublicationNotFoundException::new);

        if(!feedbackItem.getType().equals(FeedbackItemType.ATTACHMENT)){
            FeedbackItemAnswer feedbackItemAnswer = new FeedbackItemAnswer();
            feedbackItemAnswer.setItem(feedbackItem);
            feedbackItemAnswer.setPublication(feedbackPublication);
            feedbackItemAnswer.setValue(answer);

            feedbackItemAnswerRepository.save(feedbackItemAnswer);
        }
    }

    @Override
    public List<FeedbackItemAnswer> getByPublicationId(Long publicationId) {
        return this.feedbackItemAnswerRepository.findByPublicationId(publicationId);
    }

    @Override
    public List<Institution> getInstitutionsForAnswer(Long feedbackitemId, String answer) {
        List<FeedbackItemAnswer> answers = feedbackItemAnswerRepository.findByItemId(feedbackitemId);
        List<FeedbackItemAnswer> filteredAnswers = answers.stream()
                .filter(item -> item.getValue().contains(answer))
                .collect(Collectors.toList());
        List<Long> pubId = new ArrayList<>();
        filteredAnswers.forEach(item -> {
            pubId.add(item.getPublication().getId());
        });
        List<FeedbackPublication> feedbackPublications = new ArrayList<>();
        pubId.forEach(item -> {
            FeedbackPublication fp = feedbackPublicationRepository.findById(item).orElseThrow(FeedbackPublicationNotFoundException::new);
            feedbackPublications.add(fp);
        });
        List<Institution> institutions = new ArrayList<>();
        feedbackPublications.forEach(item -> {
            Institution institution = item.getInstitution();
            institutions.add(institution);
        });
        return institutions;
    }

    @Override
    public FeedbackItemAnswer saveAttachmentForAnswer(Long feedbackPublicationId, Long feedbackItemId, String name, String mimeType, Long size, byte[] content) {
        FeedbackItemAnswer feedbackItemAnswer = new FeedbackItemAnswer();

        FeedbackPublication feedbackPublication = this.feedbackPublicationRepository.findById(feedbackPublicationId).orElseThrow(FeedbackPublicationNotFoundException::new);
        FeedbackItem feedbackItem = this.feedbackItemRepository.findById(feedbackItemId).orElseThrow(FeedbackItemNotFoundException::new);

        if(feedbackItem.getType().equals(FeedbackItemType.ATTACHMENT)){
            Attachment newAttachment = new Attachment();
            newAttachment.setName(name);
            newAttachment.setMimeType(mimeType);
            newAttachment.setSize(size);
            newAttachment.setContent(content);
            attachmentRepository.save(newAttachment);

            feedbackItemAnswer.setPublication(feedbackPublication);
            feedbackItemAnswer.setItem(feedbackItem);
            feedbackItemAnswer.setAttachment(newAttachment);
            feedbackItemAnswer.setValue(newAttachment.getName());
            return feedbackItemAnswerRepository.save(feedbackItemAnswer);
        }
        return null;
    }

    @Override
    public List<FeedbackItemAnswer> findAllByFeedbackItemId(Long feedbackItemId) {
        return feedbackItemAnswerRepository.findAllByItemId(feedbackItemId);
    }


}
