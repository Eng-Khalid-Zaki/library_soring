package com.example.library.exception;

public class ExceedUserLimitException extends RuntimeException {
    public ExceedUserLimitException(String message) {
        super(message);
    }
}
