package com.demo.banking_app.domain.model;

import lombok.Value;

@Value
public class AccountId {
    Long value;
    
    public static AccountId of(Long value) {
        if (value != null && value <= 0) {
            throw new IllegalArgumentException("Account ID must be positive");
        }
        return new AccountId(value);
    }
}
