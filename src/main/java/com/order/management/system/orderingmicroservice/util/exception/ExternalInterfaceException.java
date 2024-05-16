package com.order.management.system.orderingmicroservice.util.exception;

public class ExternalInterfaceException extends Exception {

    public ExternalInterfaceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExternalInterfaceException(Throwable cause) {
        super(cause);
    }
}
