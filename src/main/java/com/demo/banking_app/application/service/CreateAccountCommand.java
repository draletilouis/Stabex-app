package com.demo.banking_app.application.service;

import com.demo.banking_app.domain.model.AccountHolderName;
import com.demo.banking_app.domain.model.AccountType;
import com.demo.banking_app.domain.model.Email;
import com.demo.banking_app.domain.model.PhoneNumber;
import lombok.Value;

@Value
public class CreateAccountCommand {
    AccountHolderName holderName;
    Email email;
    PhoneNumber phoneNumber;
    AccountType accountType;
    
    public static CreateAccountCommand of(String holderName, String email, String phoneNumber, String accountType) {
        return new CreateAccountCommand(
            AccountHolderName.of(holderName),
            Email.of(email),
            PhoneNumber.of(phoneNumber),
            AccountType.valueOf(accountType)
        );
    }
}




