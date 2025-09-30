package com.demo.banking_app.application.service;

import com.demo.banking_app.domain.model.AccountNumber;
import com.demo.banking_app.domain.model.Money;
import lombok.Value;

@Value
public class WithdrawCommand {
    AccountNumber accountNumber;
    Money amount;
    String description;
    String idempotencyKey;
    
    public static WithdrawCommand of(String accountNumber, String amount, String description, String idempotencyKey) {
        return new WithdrawCommand(
            AccountNumber.of(accountNumber),
            Money.of(new java.math.BigDecimal(amount)),
            description,
            idempotencyKey
        );
    }
}




