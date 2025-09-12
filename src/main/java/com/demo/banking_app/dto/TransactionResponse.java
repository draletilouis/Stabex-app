package com.demo.banking_app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {
    
    private String accountNumber;
    private String transactionType;
    private BigDecimal amount;
    private BigDecimal newBalance;
    private String description;
    private LocalDateTime timestamp;
}
