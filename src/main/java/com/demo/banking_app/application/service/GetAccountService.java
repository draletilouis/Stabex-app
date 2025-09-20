package com.demo.banking_app.application.service;

import com.demo.banking_app.application.port.in.GetAccountUseCase;
import com.demo.banking_app.application.port.out.AccountRepository;
import com.demo.banking_app.domain.exception.AccountNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetAccountService implements GetAccountUseCase {
    
    private final AccountRepository accountRepository;
    
    @Override
    @Transactional(readOnly = true)
    public GetAccountResponse getAccount(GetAccountCommand command) {
        log.info("Fetching account: {}", command.getAccountNumber().getValue());
        
        return accountRepository.findByAccountNumber(command.getAccountNumber())
                .map(GetAccountResponse::from)
                .orElseThrow(() -> new AccountNotFoundException("Account with number " + command.getAccountNumber().getValue() + " not found"));
    }
}


