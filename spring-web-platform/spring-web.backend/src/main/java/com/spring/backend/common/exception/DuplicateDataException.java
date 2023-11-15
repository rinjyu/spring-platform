package com.spring.backend.common.exception;

public class DuplicateDataException extends AbstractException {

    public DuplicateDataException(String message) {
        super(message);
    }

    public DuplicateDataException(int status, String message) {
        super(status, message);
    }
}