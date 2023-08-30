package com.kafein.intern.postinger_identity_service.exceptions;

public class AuthorityWithSameDescriptionExistsException extends RuntimeException {
    private String message;
    public AuthorityWithSameDescriptionExistsException() {}
    public AuthorityWithSameDescriptionExistsException(String msg) {
        super(msg);
        this.message = msg;
    }
}