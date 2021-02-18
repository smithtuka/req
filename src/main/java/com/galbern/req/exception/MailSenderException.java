package com.galbern.req.exception;

public class MailSenderException extends RuntimeException {
    private String message;
    public MailSenderException() {
        super();
    }

    public MailSenderException(String message) {
        super(message);
        this.message = message;
    }

    public MailSenderException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }

    public MailSenderException(Throwable cause) {
        super(cause);
    }
}
