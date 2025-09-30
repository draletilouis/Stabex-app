package com.demo.banking_app.infrastructure.persistence;

import com.demo.banking_app.domain.model.*;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {
    
    public Account toDomain(AccountEntity entity) {
        return Account.builder()
                .id(AccountId.of(entity.getId()))
                .accountNumber(AccountNumber.of(entity.getAccountNumber()))
                .holderName(AccountHolderName.of(entity.getAccountHolderName()))
                .email(Email.of(entity.getEmail()))
                .phoneNumber(entity.getPhoneNumber() != null ? PhoneNumber.of(entity.getPhoneNumber()) : null)
                .balance(Money.of(entity.getBalance()))
                .type(AccountType.valueOf(entity.getAccountType().name()))
                .status(AccountStatus.valueOf(entity.getStatus().name()))
                .version(Version.of(entity.getVersion()))
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
    
    public AccountEntity toEntity(Account domain) {
        return AccountEntity.builder()
                .id(domain.getId() != null ? domain.getId().getValue() : null)
                .accountNumber(domain.getAccountNumber().getValue())
                .accountNumberHash(domain.getAccountNumber().getHash())
                .accountHolderName(domain.getHolderName().getValue())
                .email(domain.getEmail().getValue())
                .phoneNumber(domain.getPhoneNumber() != null ? domain.getPhoneNumber().getValue() : null)
                .emailHash(domain.getEmail().getHash())
                .phoneHash(domain.getPhoneNumber() != null ? domain.getPhoneNumber().getHash() : null)
                .balance(domain.getBalance().getAmount())
                .accountType(AccountEntity.AccountType.valueOf(domain.getType().name()))
                .status(AccountEntity.AccountStatus.valueOf(domain.getStatus().name()))
                .version(domain.getVersion().getValue())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }
}



