package com.galbern.req.exception;

public class UsersAlreadyExistsException extends Throwable {
    private String message;
    public UsersAlreadyExistsException(String s) {
    }

    public UsersAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }

    public UsersAlreadyExistsException(Throwable cause) {
        super(cause);
    }

    protected UsersAlreadyExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
