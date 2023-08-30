package com.kafein.intern.postinger_identity_service.exceptions;

public class RoleWithSameDescriptionExistsException extends RuntimeException {
    private String message;
    public RoleWithSameDescriptionExistsException() {}
    public RoleWithSameDescriptionExistsException(String msg) {
        super(msg);
        this.message = msg;
    }
}
