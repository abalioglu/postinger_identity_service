package com.kafein.intern.postinger_identity_service.exceptions;

public class RoleWithSameNameExistsException extends RuntimeException {
    private String message;
    public RoleWithSameNameExistsException() {}
    public RoleWithSameNameExistsException(String msg) {
        super(msg);
        this.message = msg;
    }
}