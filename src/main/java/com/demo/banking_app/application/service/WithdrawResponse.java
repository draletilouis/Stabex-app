package com.demo.banking_app.application.service;

import com.demo.banking_app.domain.model.Account;
import lombok.Value;

import java.time.LocalDateTime;

@Value
public class WithdrawResponse {
    String accountNumber;
    String transactionType;
    String amount;
    String newBalance;
    String description;
    String idempotencyKey;
    LocalDateTime timestamp;
    
    public static WithdrawResponse from(Account account, WithdrawCommand command) {
        return new WithdrawResponse(
            account.getAccountNumber().getValue(),
            "WITHDRAWAL",
            command.getAmount().getAmount().toString(),
            account.getBalance().getAmount().toString(),
            command.getDescription(),
            command.getIdempotencyKey(),
            LocalDateTime.now()
        );
    }
}


