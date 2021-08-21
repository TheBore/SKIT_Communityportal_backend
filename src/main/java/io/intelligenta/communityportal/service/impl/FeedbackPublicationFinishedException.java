package io.intelligenta.communityportal.service.impl;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.EXPECTATION_FAILED)
public class FeedbackPublicationFinishedException extends RuntimeException {

    public FeedbackPublicationFinishedException() {
        super("Can not save answers for finished feedback!");
    }
}
