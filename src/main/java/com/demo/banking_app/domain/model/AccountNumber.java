package com.demo.banking_app.domain.model;

import lombok.Value;
import java.util.UUID;

@Value
public class AccountNumber {
    String value;
    String hash;
    
    private AccountNumber(String value, String hash) {
        this.value = value;
        this.hash = hash;
    }
    
    public static AccountNumber generate() {
        String accountNumber = String.format("%010d", Math.abs(UUID.randomUUID().hashCode()) % 10000000000L);
        String hash = generateHash(accountNumber);
        return new AccountNumber(accountNumber, hash);
    }
    
    public static AccountNumber of(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Account number cannot be null or empty");
        }
        String hash = generateHash(value);
        return new AccountNumber(value, hash);
    }
    
    private static String generateHash(String value) {
        // Simple hash for demo - in production use proper hashing
        return String.valueOf(value.hashCode());
    }
}

