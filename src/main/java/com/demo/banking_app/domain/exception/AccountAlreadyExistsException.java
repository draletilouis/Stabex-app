package com.demo.banking_app.domain.exception;

public class AccountAlreadyExistsException extends DomainException {
    
    public AccountAlreadyExistsException() {
        super("Account already exists");
    }
    
    public AccountAlreadyExistsException(String message) {
        super(message);
    }
}

