package com.demo.banking_app.dto;

import com.demo.banking_app.entity.Account;
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
    private Account.AccountType accountType;
    private Account.AccountStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
