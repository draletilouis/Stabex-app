package com.demo.banking_app.application.service;

import com.demo.banking_app.domain.model.AccountNumber;
import lombok.Value;

@Value
public class GetAccountCommand {
    AccountNumber accountNumber;
    
    public static GetAccountCommand of(String accountNumber) {
        return new GetAccountCommand(AccountNumber.of(accountNumber));
    }
}



