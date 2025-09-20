package com.demo.banking_app.domain.model;

import lombok.Value;

@Value
public class PhoneNumber {
    String value;
    String hash;
    
    private PhoneNumber(String value, String hash) {
        this.value = value;
        this.hash = hash;
    }
    
    public static PhoneNumber of(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null; // Phone number is optional
        }
        String hash = generateHash(value);
        return new PhoneNumber(value.trim(), hash);
    }
    
    private static String generateHash(String value) {
        return String.valueOf(value.hashCode());
    }
}

