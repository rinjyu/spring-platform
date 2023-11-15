package com.spring.backend.common.exception;

public class BizException extends AbstractException {

    public BizException(int status, String message) {
        super(status, message);
    }

    public BizException(int status, String message, Throwable throwable) {
        super(status, message, throwable);
    }
}
