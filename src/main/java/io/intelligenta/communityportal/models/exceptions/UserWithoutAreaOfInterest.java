package io.intelligenta.communityportal.models.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserWithoutAreaOfInterest extends RuntimeException{
    public UserWithoutAreaOfInterest() {
        super("It is not allowed to create User without at least one Area Of Interest!");
    }
}
