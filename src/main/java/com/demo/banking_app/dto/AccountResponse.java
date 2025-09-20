package com.demo.banking_app.dto;

import com.demo.banking_app.domain.model.AccountType;
import com.demo.banking_app.domain.model.AccountStatus;
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
public class AccountResponse {
    
    private Long id;
    private String accountNumber;
    private String accountHolderName;
    private String email;
    private String phoneNumber;
    private BigDecimal balance;
    private AccountType accountType;
    private AccountStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
