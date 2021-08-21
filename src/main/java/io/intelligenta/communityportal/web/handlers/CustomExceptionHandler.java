package io.intelligenta.communityportal.web.handlers;

import io.intelligenta.communityportal.models.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Zeljko Nikoloski (nikoloskizeljko@gmail.com)
 */
@ControllerAdvice
public class CustomExceptionHandler {


    @ResponseBody
    @ExceptionHandler(InstitutionCategoryParentChildException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> InstitutionCategoryParenChildException() {
        return new ResponseEntity<>("Недозволено циклично поврзуванје на елементите", HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @ExceptionHandler(InstitutionParentChildException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> InstitutionParenChildException() {
        return new ResponseEntity<>("Недозволено циклично поврзуванје на елементите", HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @ExceptionHandler(PublicYearReportAlreadyExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> publicYearReportException() {
        return new ResponseEntity<>("Report exists", HttpStatus.BAD_REQUEST);
    }


    @ResponseBody
    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> userExistsException() {
        return new ResponseEntity<>("User already exists", HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @ExceptionHandler(InstitutionAlreadyHaveOneModeratorException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> oneModeratorAlreadyExistsException() {
        return new ResponseEntity<>("Веќе постои службено лице за избраната институција", HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @ExceptionHandler(PasswordsNotTheSameException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> passwordsNotTheSameException() {
        return new ResponseEntity<>("Passwords doesn't match", HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @ExceptionHandler(InvalidCurrentPassword.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> invalidPasswordException() {
        return new ResponseEntity<>("Invalid current password", HttpStatus.BAD_REQUEST);
    }


}
