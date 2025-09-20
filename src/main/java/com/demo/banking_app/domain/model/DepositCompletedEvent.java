package com.demo.banking_app.domain.model;

import lombok.Getter;

@Getter
public class DepositCompletedEvent extends DomainEvent {
    private final AccountId accountId;
    private final AccountNumber accountNumber;
    private final Money amount;
    private final Money newBalance;
    
    public DepositCompletedEvent(Account account, Money amount) {
        this.accountId = account.getId();
        this.accountNumber = account.getAccountNumber();
        this.amount = amount;
        this.newBalance = account.getBalance();
    }
}


