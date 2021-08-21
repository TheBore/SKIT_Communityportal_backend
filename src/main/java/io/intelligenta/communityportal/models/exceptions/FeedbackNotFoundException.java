package io.intelligenta.communityportal.models.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Zeljko Nikoloski (nikoloskizeljko@gmail.com)
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class FeedbackNotFoundException extends RuntimeException {
}
