package com.kafein.intern.postinger_identity_service.exceptions;

public class UserWithSameEmailAlreadyExistsException  extends RuntimeException{
        private String message;
        public UserWithSameEmailAlreadyExistsException() {}
        public UserWithSameEmailAlreadyExistsException(String msg) {
            super(msg);
            this.message = msg;
        }
}
