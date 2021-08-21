package io.intelligenta.communityportal.service.impl;

import io.intelligenta.communityportal.models.Institution;
import io.intelligenta.communityportal.models.InstitutionCategory;
import io.intelligenta.communityportal.models.Tag;
import io.intelligenta.communityportal.models.auth.User;
import io.intelligenta.communityportal.models.dto.FeedbackPubDto;
import io.intelligenta.communityportal.models.exceptions.*;
import io.intelligenta.communityportal.models.feedback.Feedback;
import io.intelligenta.communityportal.models.feedback.FeedbackItem;
import io.intelligenta.communityportal.models.feedback.FeedbackPublication;
import io.intelligenta.communityportal.repository.*;
import io.intelligenta.communityportal.repository.Mail.EmailRepository;
import io.intelligenta.communityportal.repository.auth.UserRepository;
import io.intelligenta.communityportal.service.FeedbackItemAnswerService;
import io.intelligenta.communityportal.service.FeedbackPublicationService;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Zeljko Nikoloski (nikoloskizeljko@gmail.com)
 */
@Service
public class FeedbackPublicationServiceImpl implements FeedbackPublicationService {

    private final FeedbackRepository feedbackRepository;
    private final FeedbackPublicationRepository feedbackPublicationRepository;
    private final InstitutionRepository institutionRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final EmailRepository emailRepository;
    private final InstitutionCategoryRepository institutionCategoryRepository;
    private final FeedbackItemAnswerService feedbackItemAnswerService;
    private final Environment environment;


    private final FeedbackItemRepository feedbackItemRepository;

    public FeedbackPublicationServiceImpl(FeedbackRepository feedbackRepository,
                                          FeedbackPublicationRepository feedbackPublicationRepository,
                                          InstitutionRepository institutionRepository,
                                          TagRepository tagRepository,
                                          UserRepository userRepository,
                                          EmailRepository emailRepository, InstitutionCategoryRepository institutionCategoryRepository, FeedbackItemAnswerService feedbackItemAnswerService, Environment environment, FeedbackItemRepository feedbackItemRepository) {
        this.feedbackPublicationRepository = feedbackPublicationRepository;
        this.feedbackRepository = feedbackRepository;
        this.institutionRepository = institutionRepository;
        this.tagRepository = tagRepository;
        this.userRepository = userRepository;
        this.emailRepository = emailRepository;
        this.institutionCategoryRepository = institutionCategoryRepository;
        this.feedbackItemAnswerService = feedbackItemAnswerService;
        this.environment = environment;
        this.feedbackItemRepository = feedbackItemRepository;
    }

    public void publishFeedbackToInstitution(Long feedbackId, List<Long> insIds) {
        String from = environment.getProperty("spring.mail.username");

        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(FeedbackNotFoundException::new);
        feedback.setIsPublished(true);
        feedbackRepository.save(feedback);
        List<Institution> institutions = institutionRepository.findAllById(insIds);

        for (Institution institution : institutions) {
            if (feedbackPublicationRepository.findByInstitutionIdAndFeedbackId(institution.getId(), feedback.getId()) == null) {
                FeedbackPublication fp = new FeedbackPublication();
                fp.setFeedback(feedback);
                fp.setInstitution(institution);
                fp.setRead(false);
                fp.setDateCreated(LocalDateTime.now());
                fp.setDateUpdated(LocalDateTime.now());
                feedbackPublicationRepository.save(fp);

                List<User> users = institution.getUsers();
                List<String> emails = new ArrayList<>();
                users.forEach(item -> {
                    emails.add(item.getEmail());
                });
                Map<String, String> map = new HashMap<>();
                try {
                    emailRepository.sendHtmlMail(emails, "Published Announcement", "PubAnn.html", map, from);
                } catch (Exception e) {
                    throw new EmailSendingException();
                }
            }
        }

    }


    public void publishFeedbackToTags(Long feedbackId, List<Long> tagIds) {
        String from = environment.getProperty("spring.mail.username");
        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(FeedbackNotFoundException::new);
        feedback.setIsPublished(true);
        feedbackRepository.save(feedback);
        List<Tag> tags = tagRepository.findAllById(tagIds);
        List<Institution> institutions = new ArrayList<>();
        tags.forEach((item) -> {
            institutions.addAll(institutionRepository.findAllByTags(item).stream().filter(Institution::isActive).filter(institution -> !institution.isEdited()).collect(Collectors.toList()));
        });
        for (Institution institution : institutions) {
            if (feedbackPublicationRepository.findByInstitutionIdAndFeedbackId(institution.getId(), feedback.getId()) == null) {
                FeedbackPublication fp = new FeedbackPublication();
                fp.setFeedback(feedback);
                fp.setInstitution(institution);
                fp.setRead(false);
                fp.setDateCreated(LocalDateTime.now());
                fp.setDateUpdated(LocalDateTime.now());
                feedbackPublicationRepository.save(fp);

                List<User> users = institution.getUsers();
                List<String> emails = new ArrayList<>();
                users.forEach(item -> {
                    emails.add(item.getEmail());
                });
                Map<String, String> map = new HashMap<>();
                try {
                    emailRepository.sendHtmlMail(emails, "Published Announcement", "PubAnn.html", map, from);
                } catch (Exception e) {
                    throw new EmailSendingException();
                }
            }
        }
    }

    @Override
    public void publishFeedbackToCategories(Long feedbackId, List<Long> categoryIds) {
        String from = environment.getProperty("spring.mail.username");
        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(FeedbackNotFoundException::new);
        feedback.setIsPublished(true);
        feedbackRepository.save(feedback);
        List<Institution> institutions = new ArrayList<>();
        List<BigInteger> recursiveInstitutions = new ArrayList<>();
        for (Long categoryId : categoryIds) {
            recursiveInstitutions.
                    addAll(institutionCategoryRepository.
                            getAllByCategoryRecursively(categoryId));
        }
        recursiveInstitutions.forEach((item) -> {
            InstitutionCategory institutionCategory = institutionCategoryRepository.findById(item.longValue()).orElseThrow(InstitutionCategoryNotFoundException::new);
            institutions.addAll(institutionRepository.findAllByCategory(institutionCategory).stream().filter(Institution::isActive).filter(institution -> !institution.isEdited()).collect(Collectors.toList()));
        });
        for (Institution institution : institutions) {
            if (feedbackPublicationRepository.findByInstitutionIdAndFeedbackId(institution.getId(), feedback.getId()) == null) {
                FeedbackPublication fp = new FeedbackPublication();
                fp.setFeedback(feedback);
                fp.setInstitution(institution);
                fp.setRead(false);
                fp.setDateCreated(LocalDateTime.now());
                fp.setDateUpdated(LocalDateTime.now());
                feedbackPublicationRepository.save(fp);

                List<User> users = institution.getUsers();
                List<String> emails = new ArrayList<>();
                users.forEach(item -> {
                    emails.add(item.getEmail());
                });
                Map<String, String> map = new HashMap<>();
                try {
                    emailRepository.sendHtmlMail(emails, "Published Announcement", "PubAnn.html", map, from);
                } catch (Exception e) {
                    throw new EmailSendingException();
                }
            }
        }
    }

    public void deleteFeedbackPublication(Long feedbackPubId) {
        FeedbackPublication fp = feedbackPublicationRepository.findById(feedbackPubId)
                .orElseThrow(FeedbackPublicationNotFoundException::new);
        if (fp.getReadAt() == null && fp.getRead() == false) {
            feedbackPublicationRepository.delete(fp);
        }
    }


    @Override
    public FeedbackPublication markFeedbackPublicationRead(Long feedbackPubId) {
        FeedbackPublication feedbackPublication = feedbackPublicationRepository.findById(feedbackPubId).orElseThrow(FeedbackPublicationNotFoundException::new);
        if (feedbackPublication.getReadAt() == null && feedbackPublication.getRead() == false) {
            feedbackPublication.setRead(true);
            feedbackPublication.setReadAt(LocalDateTime.now());
            feedbackPublicationRepository.save(feedbackPublication);
        }
        return feedbackPublication;
    }

    @Override
    public void reminderFeedback(Long fpinstitutionId, String message) {
        String from = environment.getProperty("spring.mail.username");

        List<User> usersToSendMail = userRepository.findAllByActiveAndInstitutionId(true, fpinstitutionId);
        List<String> emails = new ArrayList<>();
        usersToSendMail.forEach(item -> {
            emails.add(item.getEmail());
        });
        Map<String, String> map = new HashMap<>();
        map.put("message", message);
        try {
            emailRepository.sendHtmlMail(emails, "Потсетување", "ReminderFeedbackPub.html", map, from);
        } catch (Exception e) {
            throw new EmailSendingException();
        }
    }

    @Override
    public void escalateFeedback(String direktorEmail, String message) {
        String from = environment.getProperty("spring.mail.username");

        List<String> emails = new ArrayList<>();
        emails.add(direktorEmail);

        Map<String, String> map = new HashMap<>();
        map.put("message", message);

        try {
            emailRepository.sendHtmlMail(emails, "Ескалира", "EscalateFeedbackPub.html", map, from);
        } catch (Exception e) {
            throw new EmailSendingException();
        }
    }

    @Override
    public FeedbackPublication getFeedbackPubById(Long id) {
        return feedbackPublicationRepository.findById(id).orElseThrow(FeedbackPublicationNotFoundException::new);
    }

    @Override
    @Transactional
    public FeedbackPublication submitAnswers(Long id, Map<Long, String> feedbackItemIdToAnswer) {

        FeedbackPublication publication = this.getFeedbackPubById(id);
        if (publication.isFinished()) {
            throw new FeedbackPublicationFinishedException();
        }
        List<FeedbackItem> items = this.feedbackItemRepository.findByFeedbackId(publication.getFeedback().getId());
        List<FeedbackItem> unAnswered = new ArrayList<>();
        for (FeedbackItem item : items) {
            if (item.isRequired() && !feedbackItemIdToAnswer.containsKey(item.getId())) {
                unAnswered.add(item);
            }
            if(item.isRequired() && feedbackItemIdToAnswer.containsValue("")){
                unAnswered.add(item);
            }
        }
        if (!unAnswered.isEmpty()) {
            throw new IncompleteFeedbackAnswersException(unAnswered);
        }
        feedbackItemIdToAnswer.entrySet()
                .forEach(e -> feedbackItemAnswerService.saveFeedbackItemAnswer(id,
                        e.getKey(),
                        e.getValue()));

        publication.setFinished(true);
        return this.feedbackPublicationRepository.save(publication);
    }

    @Override
    public Page<FeedbackPubDto> findByInstitutionIdOrderByDateCreatedDesc(Long institutionId, LocalDate date, Pageable pageable) {
        List<FeedbackPublication> feedbackPublicationList = feedbackPublicationRepository.findAllByInstitutionId(institutionId);
        List<FeedbackPubDto> feedbackPubList = new ArrayList<>();
        feedbackPublicationList.forEach(item -> {
            FeedbackPubDto fpd = new FeedbackPubDto();
            fpd.setId(item.getId());
            fpd.setDateCreated(item.getDateCreated());
            fpd.setDateUpdated(item.getDateUpdated());
            fpd.setFinished(item.isFinished());
            fpd.setRead(item.getRead());
            fpd.setReadAt(item.getReadAt());
            fpd.setInstitution(item.getInstitution());
            fpd.setFeedback(item.getFeedback());
            fpd.setNumOfQuestions(item.getFeedback().getFeedbackItems().size());
            feedbackPubList.add(fpd);
        });
        int start = (int) pageable.getOffset();
        int end = (start + pageable.getPageSize()) > feedbackPubList.size() ? feedbackPubList.size() : (start + pageable.getPageSize());
        return new PageImpl<>(feedbackPubList.subList(start, end), pageable, feedbackPubList.size());
    }

}
