package io.intelligenta.communityportal.web;

import io.intelligenta.communityportal.models.dto.FeedbackPubDto;
import io.intelligenta.communityportal.models.feedback.FeedbackPublication;
import io.intelligenta.communityportal.repository.FeedbackPublicationRepository;
import io.intelligenta.communityportal.service.FeedbackPublicationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * @author Zeljko Nikoloski (nikoloskizeljko@gmail.com)
 */
@RestController
@RequestMapping("/rest")
public class FeedbackPublicationController {
    FeedbackPublicationService feedbackPublicationService;
    FeedbackPublicationRepository feedbackPublicationRepository;

    public FeedbackPublicationController(FeedbackPublicationService feedbackPublicationService,
                                         FeedbackPublicationRepository feedbackPublicationRepository) {
        this.feedbackPublicationService = feedbackPublicationService;
        this.feedbackPublicationRepository = feedbackPublicationRepository;
    }

    @GetMapping("/feedbackpub/paged")
    public Page<FeedbackPubDto> getAllPublicationByLoggedInstitution(@RequestParam("institutionId") Long institutionId, Pageable pageable) {
        LocalDate date = LocalDate.now();
        return feedbackPublicationService.findByInstitutionIdOrderByDateCreatedDesc(institutionId, date, pageable);
    }

    @GetMapping("/feedpub/{feedbackId}/paged")
    public Page<FeedbackPublication> getPublicationsForFeedback(@PathVariable Long feedbackId, Pageable pageable) {
        return this.feedbackPublicationRepository.findByFeedbackId(feedbackId, pageable);
    }


    @PostMapping("/feedback/{feedbackId}/institution/{insIds}")
    public void publishFeedbackToInstitution(@PathVariable("feedbackId") Long feedbackId, @PathVariable("insIds") List<Long> insIds) {
        feedbackPublicationService.publishFeedbackToInstitution(feedbackId, insIds);
    }

    @PostMapping("/feedback/{feedbackId}/tags/{tagIds}")
    public void publishFeedbackByInstitutionTags(@PathVariable("feedbackId") Long feedbackId, @PathVariable("tagIds") List<Long> tagIds) {
        feedbackPublicationService.publishFeedbackToTags(feedbackId, tagIds);
    }

    @PostMapping("/feedback/{feedbackId}/categories/{categoriesIds}")
    public void publishFeedbackByInstitutionCategories(@PathVariable("feedbackId") Long feedbackId, @PathVariable("categoriesIds") List<Long> categoriesIds) {
        feedbackPublicationService.publishFeedbackToCategories(feedbackId, categoriesIds);
    }

    @GetMapping("/getFeedPub/{feedPubId}")
    public FeedbackPublication getfeedById(@PathVariable("feedPubId") Long feedPubId) {
        return feedbackPublicationService.getFeedbackPubById(feedPubId);
    }

    @DeleteMapping
    @RequestMapping("feedbackPub/{feedbackpubId}")
    public void deleteFeedbackPublication(@PathVariable("feedbackpubId") Long feedbackpubId) {
        feedbackPublicationService.deleteFeedbackPublication(feedbackpubId);
    }


    @PostMapping("/feedbackPub/markRead/{id}")
    public FeedbackPublication markRead(@PathVariable(value = "id") Long feedbackPubId) {
        return feedbackPublicationService.markFeedbackPublicationRead(feedbackPubId);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/feedback/mail/reminder")
    public void reminderFeedback(@RequestParam("fpinstitutionId") Long fpinstitutionId, @RequestParam("message") String message) {
        feedbackPublicationService.reminderFeedback(fpinstitutionId, message);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/feedback/mail/escalate")
    public void escalateFeedback(@RequestParam("direktorEmail") String direktorEmail, @RequestParam("message") String message) {
        feedbackPublicationService.escalateFeedback(direktorEmail, message);
    }
}
