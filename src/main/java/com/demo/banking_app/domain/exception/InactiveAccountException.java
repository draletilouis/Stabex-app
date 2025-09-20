package com.demo.banking_app.domain.exception;

public class InactiveAccountException extends DomainException {
    
    public InactiveAccountException() {
        super("Cannot perform operation on inactive account");
    }
    
    public InactiveAccountException(String message) {
        super(message);
    }
}

