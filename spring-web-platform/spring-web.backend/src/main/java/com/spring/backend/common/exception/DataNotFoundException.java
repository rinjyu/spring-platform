package com.spring.backend.common.exception;

public class DataNotFoundException extends AbstractException {

    public DataNotFoundException(String message) {
        super(message);
    }

    public DataNotFoundException(int status, String message) {
        super(status, message);
    }
}
