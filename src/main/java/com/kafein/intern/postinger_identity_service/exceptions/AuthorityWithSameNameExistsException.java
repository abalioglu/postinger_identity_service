package com.kafein.intern.postinger_identity_service.exceptions;

public class AuthorityWithSameNameExistsException extends RuntimeException {
    private String message;
    public AuthorityWithSameNameExistsException() {}
    public AuthorityWithSameNameExistsException(String msg) {
        super(msg);
        this.message = msg;
    }
}