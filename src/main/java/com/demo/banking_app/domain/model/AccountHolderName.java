package com.demo.banking_app.domain.model;

import lombok.Value;

@Value
public class AccountHolderName {
    String value;
    
    private AccountHolderName(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Account holder name cannot be null or empty");
        }
        if (value.trim().length() < 2) {
            throw new IllegalArgumentException("Account holder name must be at least 2 characters");
        }
        this.value = value.trim();
    }
    
    public static AccountHolderName of(String value) {
        return new AccountHolderName(value);
    }
}

