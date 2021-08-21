package io.intelligenta.communityportal.models.exceptions;


import io.intelligenta.communityportal.models.feedback.FeedbackItem;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.stream.Collectors;

@Data
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class IncompleteFeedbackAnswersException extends RuntimeException {


    private final List<FeedbackItem> unAnswered;

    public IncompleteFeedbackAnswersException(List<FeedbackItem> unAnswered) {
        super("Unanswered: " + unAnswered.stream().map(i -> i.getName()).collect(Collectors.joining(", ")));
        this.unAnswered = unAnswered;
    }
}
