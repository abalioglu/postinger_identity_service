package com.kafein.intern.postinger_identity_service.exceptions;

public class AuthorityDoesntExistException extends RuntimeException {
    private String message;
    public AuthorityDoesntExistException() {}
    public AuthorityDoesntExistException(String msg) {
        super(msg);
        this.message = msg;
    }
}
