package io.intelligenta.communityportal.web;

import io.intelligenta.communityportal.models.Institution;
import io.intelligenta.communityportal.models.feedback.FeedbackItemAnswer;
import io.intelligenta.communityportal.models.feedback.FeedbackPublication;
import io.intelligenta.communityportal.service.FeedbackItemAnswerService;
import io.intelligenta.communityportal.service.FeedbackPublicationService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/rest")
public class FeedbackItemAnswerController {

    private final FeedbackItemAnswerService feedbackItemAnswerService;
    private final FeedbackPublicationService feedbackPublicationService;

    public FeedbackItemAnswerController(FeedbackItemAnswerService feedbackItemAnswerService, FeedbackPublicationService feedbackPublicationService) {
        this.feedbackItemAnswerService = feedbackItemAnswerService;
        this.feedbackPublicationService = feedbackPublicationService;
    }

    @GetMapping("/getFeedItemAnswer/{feedItemAnsw}")
    public FeedbackItemAnswer getFeedbackItemAnswer(@PathVariable("feedItemAnsw") Long feedItemAnsw) {
        return feedbackItemAnswerService.getFeedbackItemAnswerById(feedItemAnsw);
    }

    @GetMapping("/getByPublication/{publicationId}")
    public List<FeedbackItemAnswer> getByPublicationId(@PathVariable("publicationId") Long publicationId) {
        return feedbackItemAnswerService.getByPublicationId(publicationId);
    }


    @PostMapping("/saveFeedbackAnswers/{feedbackPublicationId}")
    public FeedbackPublication saveFeedbackAnswers(@PathVariable Long feedbackPublicationId,
                                                   @RequestBody Map<Long, String> feedbackItemIdToAnswer) {
        return this.feedbackPublicationService.submitAnswers(feedbackPublicationId, feedbackItemIdToAnswer);
    }
    @GetMapping("/answer/institutions")
    public List<Institution> getInstitutionUsersForAnswer(@RequestParam("feedbackitemid")Long feedbackitemId,
                                                          @RequestParam("answer")String answer){
        return this.feedbackItemAnswerService.getInstitutionsForAnswer(feedbackitemId,answer);
    }

    @PostMapping("/answer/addAttachment")
    public FeedbackItemAnswer saveAttachmentForAnswer(@RequestParam Long feedbackPublicationId, @RequestParam Long feedbackItemId, @RequestParam("attachment") MultipartFile attachment) throws IOException {
        byte[] content = attachment.getBytes();
        String name = attachment.getOriginalFilename();
        String mimeType = attachment.getContentType();
        Long size = attachment.getSize();

        return feedbackItemAnswerService.saveAttachmentForAnswer(feedbackPublicationId, feedbackItemId, name, mimeType, size, content);
    }

    @GetMapping("/feedbackitem/answer/{feedbackItemId}")
    public List<FeedbackItemAnswer> feedbackItemAnswers(@PathVariable Long feedbackItemId){
        return feedbackItemAnswerService.findAllByFeedbackItemId(feedbackItemId);
    }
}
