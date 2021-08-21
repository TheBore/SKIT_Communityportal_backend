package io.intelligenta.communityportal.models.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProblemWithoutNAPException extends RuntimeException{
    public ProblemWithoutNAPException (){
        super("It is not allowed to create Problem without a NAP!");
    }
}
