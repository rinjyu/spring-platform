package com.spring.backend.common.exception;

import lombok.Getter;

@Getter
public class AbstractException extends RuntimeException {

    private int status;
    private String message;

    public AbstractException(String message) {
        super(message);
        this.message = message;
    }

    public AbstractException(int status, String message) {
        super(message);
        this.status = status;
    }

    protected AbstractException(String message, Throwable throwable) {
        super(message, throwable);
        this.message = message;
    }

    protected AbstractException(int status, String message, Throwable throwable) {
        this(message, throwable);
        this.status = status;
    }
}
