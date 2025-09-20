package com.demo.banking_app.application.service;

import com.demo.banking_app.application.port.in.CreateAccountUseCase;
import com.demo.banking_app.application.port.out.AccountRepository;
import com.demo.banking_app.application.port.out.EventPublisher;
import com.demo.banking_app.domain.exception.AccountAlreadyExistsException;
import com.demo.banking_app.domain.model.Account;
import com.demo.banking_app.domain.model.AccountCreatedEvent;
import com.demo.banking_app.domain.model.AccountId;
import com.demo.banking_app.domain.model.AccountNumber;
import com.demo.banking_app.domain.model.AccountStatus;
import com.demo.banking_app.domain.model.Money;
import com.demo.banking_app.domain.model.Version;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateAccountService implements CreateAccountUseCase {
    
    private final AccountRepository accountRepository;
    private final EventPublisher eventPublisher;
    
    @Override
    @Transactional
    public CreateAccountResponse createAccount(CreateAccountCommand command) {
        log.info("Creating account for email: {}", command.getEmail().getValue());
        
        // Check if account already exists
        if (accountRepository.existsByEmail(command.getEmail())) {
            throw new AccountAlreadyExistsException("Account with email " + command.getEmail().getValue() + " already exists");
        }
        
        // Generate account number
        AccountNumber accountNumber = AccountNumber.generate();
        
        // Check if account number already exists (very unlikely but possible)
        while (accountRepository.existsByAccountNumber(accountNumber)) {
            accountNumber = AccountNumber.generate();
        }
        
        // Create domain entity
        Account account = Account.builder()
                .id(null) // Let the database generate the ID
                .accountNumber(accountNumber)
                .holderName(command.getHolderName())
                .email(command.getEmail())
                .phoneNumber(command.getPhoneNumber())
                .balance(Money.zero())
                .type(command.getAccountType())
                .status(AccountStatus.ACTIVE)
                .version(Version.initial())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        
        // Save account
        Account savedAccount = accountRepository.save(account);
        
        // Publish domain event
        eventPublisher.publish(new AccountCreatedEvent(savedAccount));
        
        log.info("Account created successfully with account number: {}", savedAccount.getAccountNumber().getValue());
        
        return CreateAccountResponse.from(savedAccount);
    }
}
