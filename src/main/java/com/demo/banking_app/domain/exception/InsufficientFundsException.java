package com.demo.banking_app.domain.exception;

import com.demo.banking_app.domain.model.Money;

public class InsufficientFundsException extends DomainException {
    
    private final Money currentBalance;
    private final Money requestedAmount;
    
    public InsufficientFundsException(Money currentBalance, Money requestedAmount) {
        super(String.format("Insufficient funds. Current balance: %s, Requested amount: %s", 
                currentBalance.getAmount(), requestedAmount.getAmount()));
        this.currentBalance = currentBalance;
        this.requestedAmount = requestedAmount;
    }
    
    public Money getCurrentBalance() {
        return currentBalance;
    }
    
    public Money getRequestedAmount() {
        return requestedAmount;
    }
}

