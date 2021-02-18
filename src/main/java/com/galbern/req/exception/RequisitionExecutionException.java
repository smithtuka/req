package com.galbern.req.exception;

public class RequisitionExecutionException extends RuntimeException{
    private String message;
    public RequisitionExecutionException() {
        super();
    }

    public RequisitionExecutionException(String message) {
        super(message);
        this.message = message;
    }

    public RequisitionExecutionException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }

    public RequisitionExecutionException(Throwable cause) {
        super(cause);
    }
}
