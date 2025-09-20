package com.demo.banking_app.domain.model;

import lombok.Getter;

@Getter
public class AccountCreatedEvent extends DomainEvent {
    private final AccountId accountId;
    private final AccountNumber accountNumber;
    private final Email email;
    
    public AccountCreatedEvent(Account account) {
        this.accountId = account.getId();
        this.accountNumber = account.getAccountNumber();
        this.email = account.getEmail();
    }
}


