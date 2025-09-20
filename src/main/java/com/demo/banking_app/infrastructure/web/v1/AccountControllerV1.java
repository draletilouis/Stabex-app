package com.demo.banking_app.infrastructure.web.v1;

import com.demo.banking_app.application.port.in.CreateAccountUseCase;
import com.demo.banking_app.application.port.in.DepositUseCase;
import com.demo.banking_app.application.port.in.GetAccountUseCase;
import com.demo.banking_app.application.port.in.WithdrawUseCase;
import com.demo.banking_app.application.service.*;
import com.demo.banking_app.dto.CreateAccountRequest;
import com.demo.banking_app.dto.TransactionRequest;
import com.demo.banking_app.dto.TransactionResponse;
import com.demo.banking_app.dto.AccountResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
@Slf4j
@Deprecated(since = "2.0", forRemoval = true)
public class AccountControllerV1 {
    
    private final CreateAccountUseCase createAccountUseCase;
    private final DepositUseCase depositUseCase;
    private final WithdrawUseCase withdrawUseCase;
    private final GetAccountUseCase getAccountUseCase;
    
    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@RequestBody CreateAccountRequest request) {
        log.info("Creating new account for: {}", request.getEmail());
        
        var command = CreateAccountCommand.of(
                request.getAccountHolderName(),
                request.getEmail(),
                request.getPhoneNumber(),
                request.getAccountType().name()
        );
        
        var response = createAccountUseCase.createAccount(command);
        var accountResponse = AccountResponse.builder()
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
                .header("X-API-Version", "1")
                .header("X-Deprecation", "true")
                .header("X-Sunset", "2025-12-31")
                .header("X-Migration-Guide", "https://docs.example.com/migration-v1-to-v2")
                .body(accountResponse);
    }
    
    @PostMapping("/{accountNumber}/deposit")
    public ResponseEntity<TransactionResponse> deposit(@PathVariable String accountNumber, @RequestBody TransactionRequest request) {
        log.info("Processing deposit request for account: {}", accountNumber);
        
        var command = DepositCommand.of(accountNumber, request.getAmount().toString(), request.getDescription(), request.getIdempotencyKey());
        var response = depositUseCase.deposit(command);
        
        var transactionResponse = TransactionResponse.builder()
                .accountNumber(response.getAccountNumber())
                .transactionType(response.getTransactionType())
                .amount(new java.math.BigDecimal(response.getAmount()))
                .newBalance(new java.math.BigDecimal(response.getNewBalance()))
                .description(response.getDescription())
                .idempotencyKey(response.getIdempotencyKey())
                .timestamp(response.getTimestamp())
                .build();
        
        return ResponseEntity.ok()
                .header("X-API-Version", "1")
                .header("X-Deprecation", "true")
                .header("X-Sunset", "2025-12-31")
                .body(transactionResponse);
    }
    
    @PostMapping("/{accountNumber}/withdraw")
    public ResponseEntity<TransactionResponse> withdraw(@PathVariable String accountNumber, @RequestBody TransactionRequest request) {
        log.info("Processing withdrawal request for account: {}", accountNumber);
        
        var command = WithdrawCommand.of(accountNumber, request.getAmount().toString(), request.getDescription(), request.getIdempotencyKey());
        var response = withdrawUseCase.withdraw(command);
        
        var transactionResponse = TransactionResponse.builder()
                .accountNumber(response.getAccountNumber())
                .transactionType(response.getTransactionType())
                .amount(new java.math.BigDecimal(response.getAmount()))
                .newBalance(new java.math.BigDecimal(response.getNewBalance()))
                .description(response.getDescription())
                .idempotencyKey(response.getIdempotencyKey())
                .timestamp(response.getTimestamp())
                .build();
        
        return ResponseEntity.ok()
                .header("X-API-Version", "1")
                .header("X-Deprecation", "true")
                .header("X-Sunset", "2025-12-31")
                .body(transactionResponse);
    }
    
    @GetMapping("/{accountNumber}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable String accountNumber) {
        log.info("Fetching account: {}", accountNumber);
        
        var command = GetAccountCommand.of(accountNumber);
        var response = getAccountUseCase.getAccount(command);
        
        var accountResponse = AccountResponse.builder()
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
                .header("X-API-Version", "1")
                .header("X-Deprecation", "true")
                .header("X-Sunset", "2025-12-31")
                .body(accountResponse);
    }
}
