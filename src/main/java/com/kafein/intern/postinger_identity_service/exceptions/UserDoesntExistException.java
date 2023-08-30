package com.kafein.intern.postinger_identity_service.exceptions;

public class UserDoesntExistException extends RuntimeException {
    private String message;
    public UserDoesntExistException() {}
    public UserDoesntExistException(String msg) {
        super(msg);
        this.message = msg;
    }
}
