package io.intelligenta.communityportal.models.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AnnouncementWithoutAreaOfInterest extends RuntimeException {
    public AnnouncementWithoutAreaOfInterest() {
        super("It is not allowed to create Announcement without Area Of Interest!");
    }
}
