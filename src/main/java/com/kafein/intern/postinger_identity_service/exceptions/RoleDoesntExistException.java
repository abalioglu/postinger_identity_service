package com.kafein.intern.postinger_identity_service.exceptions;

public class RoleDoesntExistException extends RuntimeException{
    private String message;
    public RoleDoesntExistException() {}
    public RoleDoesntExistException(String msg) {
        super(msg);
        this.message = msg;
    }
}
