package com.demo.banking_app.domain.exception;

public class AccountNotFoundException extends DomainException {
    
    public AccountNotFoundException() {
        super("Account not found");
    }
    
    public AccountNotFoundException(String message) {
        super(message);
    }
}

