package com.demo.banking_app.application.service;

import com.demo.banking_app.domain.model.Account;
import lombok.Value;

import java.time.LocalDateTime;

@Value
public class GetAccountResponse {
    Long id;
    String accountNumber;
    String holderName;
    String email;
    String phoneNumber;
    String balance;
    String accountType;
    String status;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    
    public static GetAccountResponse from(Account account) {
        return new GetAccountResponse(
            account.getId().getValue(),
            account.getAccountNumber().getValue(),
            account.getHolderName().getValue(),
            account.getEmail().getValue(),
            account.getPhoneNumber() != null ? account.getPhoneNumber().getValue() : null,
            account.getBalance().getAmount().toString(),
            account.getType().name(),
            account.getStatus().name(),
            account.getCreatedAt(),
            account.getUpdatedAt()
        );
    }
}

