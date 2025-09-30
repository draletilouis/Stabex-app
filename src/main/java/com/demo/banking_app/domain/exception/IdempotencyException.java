package com.demo.banking_app.domain.exception;

public class IdempotencyException extends DomainException {
    
    public IdempotencyException(String message) {
        super(message);
    }
    
    public IdempotencyException(String message, Throwable cause) {
        super(message, cause);
    }
}




