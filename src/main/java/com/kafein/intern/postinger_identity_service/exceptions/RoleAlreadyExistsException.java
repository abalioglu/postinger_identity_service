package com.kafein.intern.postinger_identity_service.exceptions;

public class RoleAlreadyExistsException extends RuntimeException {
    private String message;
    public RoleAlreadyExistsException() {}
    public RoleAlreadyExistsException(String msg) {
        super(msg);
        this.message = msg;
    }
}