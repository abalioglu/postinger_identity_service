package com.kafein.intern.postinger_identity_service.exceptions;

public class AuthorityWithSameNameAndDescriptionExistsException extends RuntimeException {
    private String message;
    public AuthorityWithSameNameAndDescriptionExistsException() {}
    public AuthorityWithSameNameAndDescriptionExistsException(String msg) {
        super(msg);
        this.message = msg;
    }
}
