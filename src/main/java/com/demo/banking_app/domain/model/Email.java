package com.demo.banking_app.domain.model;

import lombok.Value;
import java.util.regex.Pattern;

@Value
public class Email {
    String value;
    String hash;
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );
    
    private Email(String value, String hash) {
        this.value = value;
        this.hash = hash;
    }
    
    public static Email of(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        if (!EMAIL_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("Invalid email format");
        }
        String hash = generateHash(value);
        return new Email(value, hash);
    }
    
    private static String generateHash(String value) {
        return String.valueOf(value.hashCode());
    }
}

