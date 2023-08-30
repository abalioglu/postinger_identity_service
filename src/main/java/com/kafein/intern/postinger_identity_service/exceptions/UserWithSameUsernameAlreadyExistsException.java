package com.kafein.intern.postinger_identity_service.exceptions;

public class UserWithSameUsernameAlreadyExistsException  extends RuntimeException {
    private String message;
    public UserWithSameUsernameAlreadyExistsException() {}
    public UserWithSameUsernameAlreadyExistsException(String msg) {
        super(msg);
        this.message = msg;
    }
}