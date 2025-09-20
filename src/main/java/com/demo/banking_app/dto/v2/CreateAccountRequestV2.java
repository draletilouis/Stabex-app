package com.demo.banking_app.dto.v2;

import com.demo.banking_app.domain.model.AccountType;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Enhanced Create Account Request DTO for API v2
 * Includes additional fields and validation
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountRequestV2 {
    
    @JsonProperty("holder_name")
    @NotBlank(message = "Account holder name is required")
    @Size(min = 2, max = 100, message = "Account holder name must be between 2 and 100 characters")
    private String accountHolderName;
    
    @JsonProperty("email_address")
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
    
    @JsonProperty("phone")
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Phone number should be valid")
    private String phoneNumber;
    
    @JsonProperty("account_type")
    @NotNull(message = "Account type is required")
    private AccountType accountType;
    
    // V2 specific fields
    @JsonProperty("account_holder_id")
    @NotBlank(message = "Account holder ID is required")
    @Size(min = 5, max = 20, message = "Account holder ID must be between 5 and 20 characters")
    private String accountHolderId;
    
    @JsonProperty("branch_code")
    @NotBlank(message = "Branch code is required")
    @Size(min = 3, max = 10, message = "Branch code must be between 3 and 10 characters")
    private String branchCode;
    
    @JsonProperty("currency")
    @NotBlank(message = "Currency is required")
    @Size(min = 3, max = 3, message = "Currency must be a 3-letter code")
    private String currency;
    
    @JsonProperty("initial_deposit")
    @DecimalMin(value = "0.0", inclusive = true, message = "Initial deposit must be non-negative")
    @DecimalMax(value = "1000000.0", message = "Initial deposit cannot exceed 1,000,000")
    private BigDecimal initialDeposit;
    
    @JsonProperty("interest_rate")
    @DecimalMin(value = "0.0", inclusive = true, message = "Interest rate must be non-negative")
    @DecimalMax(value = "50.0", message = "Interest rate cannot exceed 50%")
    private BigDecimal interestRate;
}
