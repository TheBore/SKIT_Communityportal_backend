package io.intelligenta.communityportal.service;

import io.intelligenta.communityportal.models.dto.FeedbackAnalysisDto;
import io.intelligenta.communityportal.models.dto.FeedbackDto;
import io.intelligenta.communityportal.models.feedback.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Zeljko Nikoloski (nikoloskizeljko@gmail.com)
 */
public interface FeedbackService {
    Feedback findFeedbackById(Long feedbackId);

    List<Feedback> getAll();

    Feedback updateFeedback(FeedbackDto feedbackDto);

    void deleteFeedback(Long id);

    Feedback addFeedback(String name, String description, Long areaOfInterestId, LocalDate dueDate);

    FeedbackAnalysisDto getAllFeedbackItemAnswersByFeedbackId(Long id);

    byte[] analyseFileToExport(Long feedbackId);

    Page<Feedback> getAllFeedbacksByUserAreasOfInterest (List<String> areas, Pageable pageable);

    Integer getNumberOfFeedbackPublishes(Long id);
}