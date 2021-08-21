package io.intelligenta.communityportal.models.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProblemWithoutNAPAreaException extends RuntimeException {
    public ProblemWithoutNAPAreaException (){
        super("It is not allowed to create Problem without NAP Area!");
    }
}
