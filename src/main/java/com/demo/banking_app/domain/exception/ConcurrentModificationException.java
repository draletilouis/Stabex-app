package com.demo.banking_app.domain.exception;

public class ConcurrentModificationException extends DomainException {
    
    public ConcurrentModificationException() {
        super("Account was modified by another transaction. Please retry.");
    }
    
    public ConcurrentModificationException(String message) {
        super(message);
    }
    
    public ConcurrentModificationException(String message, Throwable cause) {
        super(message, cause);
    }
}

