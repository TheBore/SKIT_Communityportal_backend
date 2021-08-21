package io.intelligenta.communityportal.web;

import io.intelligenta.communityportal.models.feedback.FeedbackItem;
import io.intelligenta.communityportal.models.feedback.FeedbackPublication;
import io.intelligenta.communityportal.repository.FeedbackItemRepository;
import io.intelligenta.communityportal.service.FeedbackItemService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Zeljko Nikoloski (nikoloskizeljko@gmail.com)
 */
@RestController
@CrossOrigin("*")
@RequestMapping("/rest")
public class FeedbackItemController {
    FeedbackItemService feedbackItemService;
    FeedbackItemRepository feedbackItemRepository;

    public FeedbackItemController(FeedbackItemService feedbackItemService,FeedbackItemRepository feedbackItemRepository) {
        this.feedbackItemService = feedbackItemService;
        this.feedbackItemRepository = feedbackItemRepository;
    }
    @GetMapping("/feedbackitem/all")
    public List<FeedbackItem> getAllFeedbackItemByFeedId(@RequestParam("feedbackId") Long feedbackId){
        return feedbackItemService.getAllFeedbackItemByFeedId(feedbackId);
    }

    @GetMapping("/allfeeditems/{feedbackId}/paged")
    public Page<FeedbackItem> getAllFeedItemByFeedbackId(@PathVariable("feedbackId") Long feedbackId, Pageable pageable) {
        return feedbackItemRepository.findByFeedbackIdOrderByDateCreatedDesc(feedbackId,pageable);
    }

    @PostMapping("/addfeeditem")
    public FeedbackItem addFeedbackItem(@RequestBody FeedbackItem feedbackItem, @RequestParam("feedbackId") Long feedbackId) {
        return feedbackItemService.addFeedbackItem(feedbackItem, feedbackId);
    }
    @PatchMapping("/updatefeeditem")
    public FeedbackItem updateFeedbackItem(@RequestBody FeedbackItem feedbackItem){
        return feedbackItemService.updateFeedbackItem(feedbackItem);
    }
    @DeleteMapping("/deletefeeditem/{feeditemId}")
    public void deleteFeedbackItem(@PathVariable("feeditemId")Long feeditemId){
        feedbackItemService.deleteFeedbackItem(feeditemId);
    }

    @GetMapping("/allfeedbackItems")
    public List<FeedbackItem> getAllFeedbackItemsByFeedbackPub(@RequestBody FeedbackPublication feedbackPublication) {
        return feedbackItemService.getAllFeedbackItemsByFeedbackPub(feedbackPublication);
    }
}
