package com.kafein.intern.postinger_identity_service.exceptions;

public class NoUserRegisteredException extends RuntimeException {
    private String message;
    public NoUserRegisteredException() {}
    public NoUserRegisteredException(String msg) {
        super(msg);
        this.message = msg;
    }
}