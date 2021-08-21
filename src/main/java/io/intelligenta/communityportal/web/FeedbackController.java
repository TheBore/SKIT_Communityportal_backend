package io.intelligenta.communityportal.web;

import io.intelligenta.communityportal.models.dto.FeedbackAnalysisDto;
import io.intelligenta.communityportal.models.dto.FeedbackDto;
import io.intelligenta.communityportal.models.feedback.Feedback;
import io.intelligenta.communityportal.repository.FeedbackRepository;
import io.intelligenta.communityportal.service.FeedbackService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Zeljko Nikoloski (nikoloskizeljko@gmail.com)
 */
@RestController
@RequestMapping("/rest/feedback")
public class FeedbackController {

    FeedbackService feedbackService;
    FeedbackRepository feedbackRepository;

    public FeedbackController(FeedbackService feedbackService,
                              FeedbackRepository feedbackRepository) {
        this.feedbackService = feedbackService;
        this.feedbackRepository = feedbackRepository;
    }

    @GetMapping("/{feedbackId}")
    public Feedback getFeedbackById(@PathVariable("feedbackId") Long feedbackId) {
        return feedbackService.findFeedbackById(feedbackId);
    }

    @GetMapping("/paged")
    public Page<Feedback> getFeedbackPage(Pageable pageable) {
        return feedbackRepository.findAllByOrderByDateCreatedDesc(pageable);
    }

    @GetMapping("/all")
    public List<Feedback> getAllFeedbacks() {
        return feedbackService.getAll();
    }

    @PostMapping("/add")
    public Feedback addFeedb(@RequestParam("name") String name, @RequestParam("description") String description,
                             @RequestParam("areaOfInterestId") Long areaOfInterestId,
                             @RequestParam("dueDate") @DateTimeFormat(pattern = "MM/dd/yyyy") LocalDate dueDate) {
        return feedbackService.addFeedback(name, description, areaOfInterestId, dueDate);
    }

    @DeleteMapping("/{id}")
    public void deleteFeedback(@PathVariable("id") Long id) {
        feedbackService.deleteFeedback(id);
    }

    @PatchMapping("/edit")
    public Feedback editFeedback(@RequestBody FeedbackDto feedback) {
        return feedbackService.updateFeedback(feedback);
    }

    @GetMapping("/analyse/{id}")
    public FeedbackAnalysisDto analyse(@PathVariable("id") Long feedbackId) {
        return feedbackService.getAllFeedbackItemAnswersByFeedbackId(feedbackId);
    }

    @GetMapping("/analyse/{id}/export")
    public ResponseEntity analyseExport(@PathVariable("id") Long feedbackId) {
        byte[] exportJson = feedbackService.analyseFileToExport(feedbackId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/json"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + "Analyse" + "\"")
                .body(exportJson);

    }

    @GetMapping("/feedbacks-by-areas")
    public Page<Feedback> getAllFeedbacksByAreasOfInterest (@RequestParam(value="areas") List<String> areas, Pageable pageable){
        return feedbackService.getAllFeedbacksByUserAreasOfInterest(areas, pageable);
    }

    @GetMapping("/publishes-number/{id}")
    public Integer getNumberOfFeedbackPublishes(@PathVariable Long id){
        return feedbackService.getNumberOfFeedbackPublishes(id);
    }
}
