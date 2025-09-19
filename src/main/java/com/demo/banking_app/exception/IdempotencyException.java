package com.demo.banking_app.exception;

public class IdempotencyException extends RuntimeException {
    
    public IdempotencyException(String message) {
        super(message);
    }
    
    public IdempotencyException(String message, Throwable cause) {
        super(message, cause);
    }
}
