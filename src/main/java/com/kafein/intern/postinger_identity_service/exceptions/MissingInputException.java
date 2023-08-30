package com.kafein.intern.postinger_identity_service.exceptions;

public class MissingInputException extends RuntimeException {
    private String message;
    public MissingInputException() {}
    public MissingInputException(String msg) {
        super(msg);
        this.message = msg;
    }
}