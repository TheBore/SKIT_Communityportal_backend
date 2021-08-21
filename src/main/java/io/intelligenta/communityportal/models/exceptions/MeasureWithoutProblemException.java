package io.intelligenta.communityportal.models.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MeasureWithoutProblemException extends RuntimeException {
    public MeasureWithoutProblemException() {
        super("It is not allowed to create Measure without Problem!");
    }
}
