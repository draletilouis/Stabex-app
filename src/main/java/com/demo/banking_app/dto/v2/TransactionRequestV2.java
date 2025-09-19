package com.demo.banking_app.dto.v2;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Enhanced Transaction Request DTO for API v2
 * Includes additional fields and validation
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequestV2 {
    
    @JsonProperty("account_number")
    @NotBlank(message = "Account number is required")
    @Size(min = 10, max = 20, message = "Account number must be between 10 and 20 characters")
    private String accountNumber;
    
    @JsonProperty("amount")
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    @DecimalMax(value = "1000000.0", message = "Amount cannot exceed 1,000,000")
    private BigDecimal amount;
    
    @JsonProperty("description")
    @NotBlank(message = "Description is required")
    @Size(min = 3, max = 200, message = "Description must be between 3 and 200 characters")
    private String description;
    
    // V2 specific fields
    @JsonProperty("transaction_reference")
    @Size(max = 50, message = "Transaction reference cannot exceed 50 characters")
    private String transactionReference;
    
    @JsonProperty("category")
    @Size(max = 30, message = "Category cannot exceed 30 characters")
    private String category;
    
    @JsonProperty("tags")
    @Size(max = 100, message = "Tags cannot exceed 100 characters")
    private String tags;
    
    @JsonProperty("external_reference")
    @Size(max = 50, message = "External reference cannot exceed 50 characters")
    private String externalReference;
    
    @JsonProperty("idempotency_key")
    @NotBlank(message = "Idempotency key is required")
    @Size(max = 255, message = "Idempotency key cannot exceed 255 characters")
    private String idempotencyKey;
}
