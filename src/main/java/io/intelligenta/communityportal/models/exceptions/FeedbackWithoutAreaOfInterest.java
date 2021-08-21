package io.intelligenta.communityportal.models.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FeedbackWithoutAreaOfInterest extends RuntimeException {
    public FeedbackWithoutAreaOfInterest() {
        super("It is not allowed to create Feedback without Area Of Interest!");
    }
}
