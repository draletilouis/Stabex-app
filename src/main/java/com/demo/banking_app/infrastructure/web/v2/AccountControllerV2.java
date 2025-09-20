package com.demo.banking_app.infrastructure.web.v2;

import com.demo.banking_app.application.port.in.CreateAccountUseCase;
import com.demo.banking_app.application.port.in.DepositUseCase;
import com.demo.banking_app.application.port.in.GetAccountUseCase;
import com.demo.banking_app.application.port.in.WithdrawUseCase;
import com.demo.banking_app.application.service.*;
import com.demo.banking_app.dto.v2.CreateAccountRequestV2;
import com.demo.banking_app.dto.v2.TransactionRequestV2;
import com.demo.banking_app.dto.v2.TransactionResponseV2;
import com.demo.banking_app.dto.v2.AccountResponseV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/accounts")
@RequiredArgsConstructor
@Slf4j
public class AccountControllerV2 {
    
    private final CreateAccountUseCase createAccountUseCase;
    private final DepositUseCase depositUseCase;
    private final WithdrawUseCase withdrawUseCase;
    private final GetAccountUseCase getAccountUseCase;
    
    @PostMapping
    public ResponseEntity<AccountResponseV2> createAccount(@RequestBody CreateAccountRequestV2 request) {
        log.info("Creating new account for: {}", request.getEmail());
        
        var command = CreateAccountCommand.of(
                request.getAccountHolderName(),
                request.getEmail(),
                request.getPhoneNumber(),
                request.getAccountType().name()
        );
        
        var response = createAccountUseCase.createAccount(command);
        var accountResponse = AccountResponseV2.builder()
                .id(response.getId())
                .accountNumber(response.getAccountNumber())
                .accountHolderName(response.getHolderName())
                .email(response.getEmail())
                .phoneNumber(response.getPhoneNumber())
                .balance(new java.math.BigDecimal(response.getBalance()))
                .accountType(com.demo.banking_app.domain.model.AccountType.valueOf(response.getAccountType()))
                .status(com.demo.banking_app.domain.model.AccountStatus.valueOf(response.getStatus()))
                .createdAt(response.getCreatedAt())
                .updatedAt(response.getUpdatedAt())
                .build();
        
        return ResponseEntity.ok()
                .header("X-API-Version", "2")
                .body(accountResponse);
    }
    
    @PostMapping("/{accountNumber}/deposit")
    public ResponseEntity<TransactionResponseV2> deposit(@PathVariable String accountNumber, @RequestBody TransactionRequestV2 request) {
        log.info("Processing deposit request for account: {}", accountNumber);
        
        var command = DepositCommand.of(accountNumber, request.getAmount().toString(), request.getDescription(), request.getIdempotencyKey());
        var response = depositUseCase.deposit(command);
        
        var transactionResponse = TransactionResponseV2.builder()
                .accountNumber(response.getAccountNumber())
                .transactionType(response.getTransactionType())
                .amount(new java.math.BigDecimal(response.getAmount()))
                .newBalance(new java.math.BigDecimal(response.getNewBalance()))
                .description(response.getDescription())
                .idempotencyKey(response.getIdempotencyKey())
                .timestamp(response.getTimestamp())
                .build();
        
        return ResponseEntity.ok()
                .header("X-API-Version", "2")
                .body(transactionResponse);
    }
    
    @PostMapping("/{accountNumber}/withdraw")
    public ResponseEntity<TransactionResponseV2> withdraw(@PathVariable String accountNumber, @RequestBody TransactionRequestV2 request) {
        log.info("Processing withdrawal request for account: {}", accountNumber);
        
        var command = WithdrawCommand.of(accountNumber, request.getAmount().toString(), request.getDescription(), request.getIdempotencyKey());
        var response = withdrawUseCase.withdraw(command);
        
        var transactionResponse = TransactionResponseV2.builder()
                .accountNumber(response.getAccountNumber())
                .transactionType(response.getTransactionType())
                .amount(new java.math.BigDecimal(response.getAmount()))
                .newBalance(new java.math.BigDecimal(response.getNewBalance()))
                .description(response.getDescription())
                .idempotencyKey(response.getIdempotencyKey())
                .timestamp(response.getTimestamp())
                .build();
        
        return ResponseEntity.ok()
                .header("X-API-Version", "2")
                .body(transactionResponse);
    }
    
    @GetMapping("/{accountNumber}")
    public ResponseEntity<AccountResponseV2> getAccount(@PathVariable String accountNumber) {
        log.info("Fetching account: {}", accountNumber);
        
        var command = GetAccountCommand.of(accountNumber);
        var response = getAccountUseCase.getAccount(command);
        
        var accountResponse = AccountResponseV2.builder()
                .id(response.getId())
                .accountNumber(response.getAccountNumber())
                .accountHolderName(response.getHolderName())
                .email(response.getEmail())
                .phoneNumber(response.getPhoneNumber())
                .balance(new java.math.BigDecimal(response.getBalance()))
                .accountType(com.demo.banking_app.domain.model.AccountType.valueOf(response.getAccountType()))
                .status(com.demo.banking_app.domain.model.AccountStatus.valueOf(response.getStatus()))
                .createdAt(response.getCreatedAt())
                .updatedAt(response.getUpdatedAt())
                .build();
        
        return ResponseEntity.ok()
                .header("X-API-Version", "2")
                .body(accountResponse);
    }
}
