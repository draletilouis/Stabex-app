package com.demo.banking_app.application.service;

import com.demo.banking_app.domain.model.Account;
import com.demo.banking_app.domain.model.Money;
import lombok.Value;

import java.time.LocalDateTime;

@Value
public class DepositResponse {
    String accountNumber;
    String transactionType;
    String amount;
    String newBalance;
    String description;
    String idempotencyKey;
    LocalDateTime timestamp;
    
    public static DepositResponse from(Account account, DepositCommand command) {
        return new DepositResponse(
            account.getAccountNumber().getValue(),
            "DEPOSIT",
            command.getAmount().getAmount().toString(),
            account.getBalance().getAmount().toString(),
            command.getDescription(),
            command.getIdempotencyKey(),
            LocalDateTime.now()
        );
    }
}




