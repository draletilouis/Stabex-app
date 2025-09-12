package com.demo.banking_app.service;

import com.demo.banking_app.dto.*;
import com.demo.banking_app.entity.Account;

import java.util.List;

public interface AccountService {
    
    AccountResponse createAccount(CreateAccountRequest request);
    
    AccountResponse getAccountByAccountNumber(String accountNumber);
    
    AccountResponse getAccountById(Long id);
    
    List<AccountResponse> getAllAccounts();
    
    List<AccountResponse> getAccountsByAccountHolderName(String name);
    
    TransactionResponse deposit(TransactionRequest request);
    
    TransactionResponse withdraw(TransactionRequest request);
    
    void deleteAccount(String accountNumber);
    
    AccountResponse updateAccountStatus(String accountNumber, Account.AccountStatus status);
}