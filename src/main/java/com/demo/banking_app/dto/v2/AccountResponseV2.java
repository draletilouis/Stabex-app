package com.demo.banking_app.dto.v2;

import com.demo.banking_app.entity.Account;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Enhanced Account Response DTO for API v2
 * Includes additional fields and improved structure
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponseV2 {
    
    @JsonProperty("account_id")
    private Long id;
    
    @JsonProperty("account_number")
    private String accountNumber;
    
    @JsonProperty("holder_name")
    private String accountHolderName;
    
    @JsonProperty("email_address")
    private String email;
    
    @JsonProperty("phone")
    private String phoneNumber;
    
    @JsonProperty("current_balance")
    private BigDecimal balance;
    
    @JsonProperty("account_type")
    private Account.AccountType accountType;
    
    @JsonProperty("status")
    private Account.AccountStatus status;
    
    @JsonProperty("created_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonProperty("updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
    
    // V2 specific fields
    @JsonProperty("account_holder_id")
    private String accountHolderId;
    
    @JsonProperty("branch_code")
    private String branchCode;
    
    @JsonProperty("currency")
    private String currency;
    
    @JsonProperty("interest_rate")
    private BigDecimal interestRate;
    
    @JsonProperty("last_transaction_date")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime lastTransactionDate;
    
    @JsonProperty("transaction_count")
    private Long transactionCount;
}