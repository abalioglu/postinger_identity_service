package com.kafein.intern.postinger_identity_service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({ UserAlreadyExistsException.class, UserWithSameEmailAlreadyExistsException.class,
            UserWithSameUsernameAlreadyExistsException.class, UserWithSameusernameAndEmailAlreadyExistsException.class,
            MissingInputException.class, RoleAlreadyExistsException.class,RoleWithSameDescriptionExistsException.class,
    RoleWithSameNameExistsException.class, RoleWithSameNameAndDescriptionExistsException.class, AuthorityAlreadyExistsException.class,
    AuthorityWithSameDescriptionExistsException.class, AuthorityWithSameNameAndDescriptionExistsException.class, AuthorityWithSameNameExistsException.class})
    public ResponseEntity<ErrorResponse> handleAsBadRequest(RuntimeException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(),ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({NoUserRegisteredException.class, RoleDoesntExistException.class, UserDoesntExistException.class,
    AuthorityDoesntExistException.class})
    public ResponseEntity<ErrorResponse> handleAsNotFound(RuntimeException ex) {
        ErrorResponse errorDetails = new ErrorResponse(HttpStatus.NOT_FOUND.value(),ex.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

}
