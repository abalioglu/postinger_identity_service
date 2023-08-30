package com.kafein.intern.postinger_identity_service.exceptions;

public class AuthorityAlreadyExistsException extends RuntimeException {
    private String message;
    public AuthorityAlreadyExistsException() {}
    public AuthorityAlreadyExistsException(String msg) {
        super(msg);
        this.message = msg;
    }
}