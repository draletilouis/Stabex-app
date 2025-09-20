package com.demo.banking_app.domain.model;

import lombok.Value;

@Value
public class Version {
    Long value;
    
    private Version(Long value) {
        if (value == null || value < 0) {
            throw new IllegalArgumentException("Version must be non-negative");
        }
        this.value = value;
    }
    
    public static Version of(Long value) {
        return new Version(value);
    }
    
    public static Version initial() {
        return new Version(0L);
    }
    
    public Version increment() {
        return new Version(this.value + 1);
    }
}

