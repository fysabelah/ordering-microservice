package com.order.management.system.orderingmicroservice.util.exception;

public class ProcessMessageException extends RuntimeException {

    public ProcessMessageException(Throwable cause) {
        super(cause);
    }

    public ProcessMessageException(String message) {
        super(message);
    }
}
