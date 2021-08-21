package io.intelligenta.communityportal.models.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class QuestionWithoutAreaOfInterest extends RuntimeException{
    public QuestionWithoutAreaOfInterest(){
        super("It is not allowed to create Question without Area Of Interest!");
    }
}
