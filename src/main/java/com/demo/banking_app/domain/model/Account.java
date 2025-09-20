package com.demo.banking_app.domain.model;

import com.demo.banking_app.domain.exception.InactiveAccountException;
import com.demo.banking_app.domain.exception.InsufficientFundsException;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class Account {
    
    private final AccountId id;
    private final AccountNumber accountNumber;
    private final AccountHolderName holderName;
    private final Email email;
    private final PhoneNumber phoneNumber;
    private final Money balance;
    private final AccountType type;
    private final AccountStatus status;
    private final Version version;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    
    public Account deposit(Money amount) {
        if (status != AccountStatus.ACTIVE) {
            throw new InactiveAccountException();
        }
        
        Money newBalance = balance.add(amount);
        
        return Account.builder()
                .id(this.id)
                .accountNumber(this.accountNumber)
                .holderName(this.holderName)
                .email(this.email)
                .phoneNumber(this.phoneNumber)
                .balance(newBalance)
                .type(this.type)
                .status(this.status)
                .version(this.version.increment())
                .createdAt(this.createdAt)
                .updatedAt(LocalDateTime.now())
                .build();
    }
    
    public Account withdraw(Money amount) {
        if (status != AccountStatus.ACTIVE) {
            throw new InactiveAccountException();
        }
        
        if (balance.isLessThan(amount)) {
            throw new InsufficientFundsException(balance, amount);
        }
        
        Money newBalance = balance.subtract(amount);
        
        return Account.builder()
                .id(this.id)
                .accountNumber(this.accountNumber)
                .holderName(this.holderName)
                .email(this.email)
                .phoneNumber(this.phoneNumber)
                .balance(newBalance)
                .type(this.type)
                .status(this.status)
                .version(this.version.increment())
                .createdAt(this.createdAt)
                .updatedAt(LocalDateTime.now())
                .build();
    }
    
    public Account updateStatus(AccountStatus newStatus) {
        return Account.builder()
                .id(this.id)
                .accountNumber(this.accountNumber)
                .holderName(this.holderName)
                .email(this.email)
                .phoneNumber(this.phoneNumber)
                .balance(this.balance)
                .type(this.type)
                .status(newStatus)
                .version(this.version.increment())
                .createdAt(this.createdAt)
                .updatedAt(LocalDateTime.now())
                .build();
    }
    
    public boolean isActive() {
        return status == AccountStatus.ACTIVE;
    }
}

