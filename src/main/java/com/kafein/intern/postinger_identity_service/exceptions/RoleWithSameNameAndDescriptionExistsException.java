package com.kafein.intern.postinger_identity_service.exceptions;

public class RoleWithSameNameAndDescriptionExistsException extends RuntimeException {
    private String message;
    public RoleWithSameNameAndDescriptionExistsException() {}
    public RoleWithSameNameAndDescriptionExistsException(String msg) {
        super(msg);
        this.message = msg;
    }
}
