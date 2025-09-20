package com.demo.banking_app.application.service;

import com.demo.banking_app.application.port.in.DepositUseCase;
import com.demo.banking_app.application.port.out.AccountRepository;
import com.demo.banking_app.application.port.out.EventPublisher;
import com.demo.banking_app.domain.exception.AccountNotFoundException;
import com.demo.banking_app.domain.exception.ConcurrentModificationException;
import com.demo.banking_app.domain.model.Account;
import com.demo.banking_app.domain.model.DepositCompletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Slf4j
public class DepositService implements DepositUseCase {
    
    private final AccountRepository accountRepository;
    private final EventPublisher eventPublisher;
    
    @Override
    @Transactional
    public DepositResponse deposit(DepositCommand command) {
        log.info("Processing deposit of {} for account: {} with idempotency key: {}",
                command.getAmount().getAmount(), command.getAccountNumber().getValue(), command.getIdempotencyKey());
        
        
        try {
            // Find account
            Account account = accountRepository.findByAccountNumber(command.getAccountNumber())
                    .orElseThrow(() -> new AccountNotFoundException("Account with number " + command.getAccountNumber().getValue() + " not found"));
            
            // Perform deposit (domain logic)
            Account updatedAccount = account.deposit(command.getAmount());
            
            // Save with optimistic locking
            Account savedAccount = accountRepository.save(updatedAccount);
            
            // Create response
            DepositResponse response = DepositResponse.from(savedAccount, command);
            
            
            // Publish domain event
            eventPublisher.publish(new DepositCompletedEvent(savedAccount, command.getAmount()));
            
            log.info("Deposit successful. New balance: {}", savedAccount.getBalance().getAmount());
            return response;
            
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new ConcurrentModificationException("Account was modified by another transaction. Please retry.", e);
        } catch (Exception e) {
            throw e;
        }
    }
}
