package com.demo.banking_app.controller.v2;

import com.demo.banking_app.dto.v2.*;
import com.demo.banking_app.entity.Account;
import com.demo.banking_app.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Enhanced Account Controller for API v2
 * Provides improved functionality and additional features
 */
@RestController
@RequestMapping("/api/v2/accounts")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class AccountControllerV2 {
    
    private final AccountService accountService;
    
    @PostMapping
    public ResponseEntity<AccountResponseV2> createAccount(@Valid @RequestBody CreateAccountRequestV2 request) {
        log.info("Creating new account (v2) for: {}", request.getEmail());
        // Convert v2 request to v1 format for service layer
        AccountResponseV2 response = convertToV2Response(accountService.createAccount(convertToV1Request(request)));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @GetMapping("/{accountNumber}")
    public ResponseEntity<AccountResponseV2> getAccountByAccountNumber(@PathVariable String accountNumber) {
        log.info("Fetching account details (v2) for: {}", accountNumber);
        AccountResponseV2 response = convertToV2Response(accountService.getAccountByAccountNumber(accountNumber));
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/id/{id}")
    public ResponseEntity<AccountResponseV2> getAccountById(@PathVariable Long id) {
        log.info("Fetching account details (v2) for ID: {}", id);
        AccountResponseV2 response = convertToV2Response(accountService.getAccountById(id));
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    public ResponseEntity<List<AccountResponseV2>> getAllAccounts() {
        log.info("Fetching all accounts (v2)");
        List<AccountResponseV2> responses = accountService.getAllAccounts()
            .stream()
            .map(this::convertToV2Response)
            .toList();
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<AccountResponseV2>> searchAccounts(@RequestParam String name) {
        log.info("Searching accounts (v2) by holder name: {}", name);
        List<AccountResponseV2> responses = accountService.getAccountsByAccountHolderName(name)
            .stream()
            .map(this::convertToV2Response)
            .toList();
        return ResponseEntity.ok(responses);
    }
    
    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponseV2> deposit(@Valid @RequestBody TransactionRequestV2 request) {
        log.info("Processing deposit request (v2) for account: {}", request.getAccountNumber());
        TransactionResponseV2 response = convertToV2TransactionResponse(accountService.deposit(convertToV1TransactionRequest(request)));
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponseV2> withdraw(@Valid @RequestBody TransactionRequestV2 request) {
        log.info("Processing withdrawal request (v2) for account: {}", request.getAccountNumber());
        TransactionResponseV2 response = convertToV2TransactionResponse(accountService.withdraw(convertToV1TransactionRequest(request)));
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{accountNumber}")
    public ResponseEntity<Void> deleteAccount(@PathVariable String accountNumber) {
        log.info("Deleting account (v2): {}", accountNumber);
        accountService.deleteAccount(accountNumber);
        return ResponseEntity.noContent().build();
    }
    
    @PutMapping("/{accountNumber}/status")
    public ResponseEntity<AccountResponseV2> updateAccountStatus(
            @PathVariable String accountNumber,
            @RequestParam Account.AccountStatus status) {
        log.info("Updating account {} status to {} (v2)", accountNumber, status);
        AccountResponseV2 response = convertToV2Response(accountService.updateAccountStatus(accountNumber, status));
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Banking Application v2 is running!");
    }
    
    // V2 specific endpoints
    @GetMapping("/{accountNumber}/transactions")
    public ResponseEntity<List<TransactionResponseV2>> getAccountTransactions(@PathVariable String accountNumber) {
        log.info("Fetching transactions (v2) for account: {}", accountNumber);
        // This would need to be implemented in the service layer
        return ResponseEntity.ok(List.of());
    }
    
    @GetMapping("/{accountNumber}/balance")
    public ResponseEntity<Object> getAccountBalance(@PathVariable String accountNumber) {
        log.info("Fetching balance (v2) for account: {}", accountNumber);
        AccountResponseV2 account = convertToV2Response(accountService.getAccountByAccountNumber(accountNumber));
        return ResponseEntity.ok(new BalanceResponse(
            account.getAccountNumber(),
            account.getBalance().toString(),
            account.getCurrency(),
            account.getUpdatedAt().toString()
        ));
    }
    
    // Inner class for balance response
    private static class BalanceResponse {
        public final String accountNumber;
        public final String balance;
        public final String currency;
        public final String lastUpdated;
        
        public BalanceResponse(String accountNumber, String balance, String currency, String lastUpdated) {
            this.accountNumber = accountNumber;
            this.balance = balance;
            this.currency = currency;
            this.lastUpdated = lastUpdated;
        }
    }
    
    // Conversion methods
    private AccountResponseV2 convertToV2Response(com.demo.banking_app.dto.AccountResponse v1Response) {
        return AccountResponseV2.builder()
            .id(v1Response.getId())
            .accountNumber(v1Response.getAccountNumber())
            .accountHolderName(v1Response.getAccountHolderName())
            .email(v1Response.getEmail())
            .phoneNumber(v1Response.getPhoneNumber())
            .balance(v1Response.getBalance())
            .accountType(v1Response.getAccountType())
            .status(v1Response.getStatus())
            .createdAt(v1Response.getCreatedAt())
            .updatedAt(v1Response.getUpdatedAt())
            .accountHolderId("AH" + v1Response.getId()) // Generate placeholder
            .branchCode("001") // Default branch
            .currency("USD") // Default currency
            .interestRate(java.math.BigDecimal.valueOf(2.5)) // Default interest rate
            .lastTransactionDate(v1Response.getUpdatedAt())
            .transactionCount(0L) // Would need to be calculated
            .build();
    }
    
    private com.demo.banking_app.dto.CreateAccountRequest convertToV1Request(CreateAccountRequestV2 v2Request) {
        com.demo.banking_app.dto.CreateAccountRequest request = new com.demo.banking_app.dto.CreateAccountRequest();
        request.setAccountHolderName(v2Request.getAccountHolderName());
        request.setEmail(v2Request.getEmail());
        request.setPhoneNumber(v2Request.getPhoneNumber());
        request.setAccountType(v2Request.getAccountType());
        return request;
    }
    
    private com.demo.banking_app.dto.TransactionRequest convertToV1TransactionRequest(TransactionRequestV2 v2Request) {
        com.demo.banking_app.dto.TransactionRequest request = new com.demo.banking_app.dto.TransactionRequest();
        request.setAccountNumber(v2Request.getAccountNumber());
        request.setAmount(v2Request.getAmount());
        request.setDescription(v2Request.getDescription());
        return request;
    }
    
    private TransactionResponseV2 convertToV2TransactionResponse(com.demo.banking_app.dto.TransactionResponse v1Response) {
        TransactionResponseV2 response = new TransactionResponseV2();
        response.setTransactionId("TXN" + System.currentTimeMillis());
        response.setAccountNumber(v1Response.getAccountNumber());
        response.setTransactionType(v1Response.getTransactionType());
        response.setAmount(v1Response.getAmount());
        response.setNewBalance(v1Response.getNewBalance());
        response.setDescription(v1Response.getDescription());
        response.setTimestamp(v1Response.getTimestamp());
        response.setTransactionReference("REF" + System.currentTimeMillis());
        response.setCategory("General");
        response.setTags("");
        response.setExternalReference("");
        response.setFeeAmount(java.math.BigDecimal.ZERO);
        response.setNetAmount(v1Response.getAmount());
        response.setStatus("COMPLETED");
        response.setProcessedBy("SYSTEM");
        return response;
    }
}
