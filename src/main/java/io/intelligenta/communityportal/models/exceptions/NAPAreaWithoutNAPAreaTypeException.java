package io.intelligenta.communityportal.models.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NAPAreaWithoutNAPAreaTypeException extends RuntimeException {

    public NAPAreaWithoutNAPAreaTypeException(){
        super("It is not allowed to create NAP Area without NAP Area Type!");
    }

}
