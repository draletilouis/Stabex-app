package com.demo.banking_app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Configuration for API versioning strategies
 * Supports multiple versioning approaches:
 * 1. URL Path versioning (/api/v1/, /api/v2/)
 * 2. Header-based versioning (X-API-Version)
 * 3. Content negotiation versioning
 */
@Configuration
public class ApiVersioningConfig implements WebMvcConfigurer {


    /**
     * Exception handler for invalid API versions
     */
    public static class InvalidApiVersionException extends ResponseStatusException {
        public InvalidApiVersionException(String message) {
            super(HttpStatus.BAD_REQUEST, message);
        }
    }
}