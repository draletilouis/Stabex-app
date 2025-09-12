package com.demo.banking_app.controller;

import com.demo.banking_app.dto.*;
import com.demo.banking_app.entity.Account;
import com.demo.banking_app.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class AccountController {
    
    private final AccountService accountService;
    
    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody CreateAccountRequest request) {
        log.info("Creating new account for: {}", request.getEmail());
        AccountResponse response = accountService.createAccount(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @GetMapping("/{accountNumber}")
    public ResponseEntity<AccountResponse> getAccountByAccountNumber(@PathVariable String accountNumber) {
        log.info("Fetching account details for: {}", accountNumber);
        AccountResponse response = accountService.getAccountByAccountNumber(accountNumber);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/id/{id}")
    public ResponseEntity<AccountResponse> getAccountById(@PathVariable Long id) {
        log.info("Fetching account details for ID: {}", id);
        AccountResponse response = accountService.getAccountById(id);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    public ResponseEntity<List<AccountResponse>> getAllAccounts() {
        log.info("Fetching all accounts");
        List<AccountResponse> responses = accountService.getAllAccounts();
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<AccountResponse>> searchAccountsByHolderName(@RequestParam String name) {
        log.info("Searching accounts by holder name: {}", name);
        List<AccountResponse> responses = accountService.getAccountsByAccountHolderName(name);
        return ResponseEntity.ok(responses);
    }
    
    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponse> deposit(@Valid @RequestBody TransactionRequest request) {
        log.info("Processing deposit request for account: {}", request.getAccountNumber());
        TransactionResponse response = accountService.deposit(request);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponse> withdraw(@Valid @RequestBody TransactionRequest request) {
        log.info("Processing withdrawal request for account: {}", request.getAccountNumber());
        TransactionResponse response = accountService.withdraw(request);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{accountNumber}")
    public ResponseEntity<Void> deleteAccount(@PathVariable String accountNumber) {
        log.info("Deleting account: {}", accountNumber);
        accountService.deleteAccount(accountNumber);
        return ResponseEntity.noContent().build();
    }
    
    @PutMapping("/{accountNumber}/status")
    public ResponseEntity<AccountResponse> updateAccountStatus(
            @PathVariable String accountNumber,
            @RequestParam Account.AccountStatus status) {
        log.info("Updating account {} status to {}", accountNumber, status);
        AccountResponse response = accountService.updateAccountStatus(accountNumber, status);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Banking Application is running!");
    }
}
