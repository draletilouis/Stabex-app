package com.demo.banking_app.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionRequest {
    
    // Account number is provided via path variable, not request body
    private String accountNumber;
    
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;
    
    private String description;
    
    @NotBlank(message = "Idempotency key is required")
    private String idempotencyKey;
}
