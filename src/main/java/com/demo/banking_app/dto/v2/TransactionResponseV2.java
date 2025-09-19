package com.demo.banking_app.dto.v2;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Enhanced Transaction Response DTO for API v2
 * Includes additional fields and improved structure
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponseV2 {
    
    @JsonProperty("transaction_id")
    private String transactionId;
    
    @JsonProperty("account_number")
    private String accountNumber;
    
    @JsonProperty("transaction_type")
    private String transactionType;
    
    @JsonProperty("amount")
    private BigDecimal amount;
    
    @JsonProperty("new_balance")
    private BigDecimal newBalance;
    
    @JsonProperty("description")
    private String description;
    
    @JsonProperty("timestamp")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;
    
    // V2 specific fields
    @JsonProperty("transaction_reference")
    private String transactionReference;
    
    @JsonProperty("category")
    private String category;
    
    @JsonProperty("tags")
    private String tags;
    
    @JsonProperty("external_reference")
    private String externalReference;
    
    @JsonProperty("fee_amount")
    private BigDecimal feeAmount;
    
    @JsonProperty("net_amount")
    private BigDecimal netAmount;
    
    @JsonProperty("status")
    private String status;
    
    @JsonProperty("processed_by")
    private String processedBy;
    
    @JsonProperty("idempotency_key")
    private String idempotencyKey;
}
