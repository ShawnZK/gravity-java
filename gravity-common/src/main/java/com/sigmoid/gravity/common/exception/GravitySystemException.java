package com.sigmoid.gravity.common.exception;

public class GravitySystemException extends RuntimeException {

    public GravitySystemException(Throwable cause) {
        super(cause);
    }

    public GravitySystemException(String message) {
        super(message);
    }

    public GravitySystemException(String message, Throwable cause) {
        super(message, cause);
    }

}
