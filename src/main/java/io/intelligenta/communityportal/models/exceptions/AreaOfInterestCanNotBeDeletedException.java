package io.intelligenta.communityportal.models.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.METHOD_NOT_ALLOWED)
public class AreaOfInterestCanNotBeDeletedException extends RuntimeException {
    public AreaOfInterestCanNotBeDeletedException(){

    }
}
