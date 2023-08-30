package com.kafein.intern.postinger_identity_service.exceptions;

public class UserWithSameusernameAndEmailAlreadyExistsException extends RuntimeException {
    private String message;
    public UserWithSameusernameAndEmailAlreadyExistsException() {}
    public UserWithSameusernameAndEmailAlreadyExistsException(String msg) {
        super(msg);
        this.message = msg;
    }
}
