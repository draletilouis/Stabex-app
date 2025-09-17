package com.demo.banking_app.service;

import com.demo.banking_app.dto.*;
import com.demo.banking_app.entity.Account;
import com.demo.banking_app.exception.*;
import com.demo.banking_app.repository.AccountRepository;
import com.demo.banking_app.util.DeterministicHasher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AccountServiceImpl implements AccountService {
    
    private final AccountRepository accountRepository;
    private final DeterministicHasher hasher;
    
    @Override
    public AccountResponse createAccount(CreateAccountRequest request) {
        log.info("Creating account for email: {}", request.getEmail());
        
        // Check if account with email already exists (using hash)
        String emailHash = hasher.hash(request.getEmail());
        if (accountRepository.existsByEmailHash(emailHash)) {
            throw new AccountAlreadyExistsException("Account with email " + request.getEmail() + " already exists");
        }
        
        // Generate unique account number and its hash
        String accountNumber = generateAccountNumber();
        String accountNumberHash = hasher.hash(accountNumber);
        
        // Create new account (include hash at build time so it's present in INSERT)
        Account account = Account.builder()
                .accountNumber(accountNumber)
                .accountNumberHash(accountNumberHash)
                .accountHolderName(request.getAccountHolderName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .accountType(request.getAccountType())
                .balance(BigDecimal.ZERO)
                .status(Account.AccountStatus.ACTIVE)
                .build();

        // Set deterministic hashes for lookup
        account.setEmailHash(emailHash);
        account.setPhoneHash(hasher.hash(request.getPhoneNumber()));
        // already set in builder
        log.debug("Generated accountNumber={} accountNumberHash={} emailHash={} phoneHash={}", accountNumber, accountNumberHash, emailHash, account.getPhoneHash());
        
        Account savedAccount = accountRepository.save(account);
        log.info("Account created successfully with account number: {}", accountNumber);
        
        return mapToAccountResponse(savedAccount);
    }
    
    @Override
    public AccountResponse getAccountByAccountNumber(String accountNumber) {
        log.info("Fetching account details for account number: {}", accountNumber);
        
        Account account = accountRepository.findActiveAccountByAccountNumber(hasher.hash(accountNumber))
                .orElseThrow(() -> new AccountNotFoundException("Account with number " + accountNumber + " not found"));
        
        return mapToAccountResponse(account);
    }
    
    @Override
    public AccountResponse getAccountById(Long id) {
        log.info("Fetching account details for ID: {}", id);
        
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account with ID " + id + " not found"));
        
        return mapToAccountResponse(account);
    }
    
    @Override
    public List<AccountResponse> getAllAccounts() {
        log.info("Fetching all accounts");
        
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream()
                .map(this::mapToAccountResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<AccountResponse> getAccountsByAccountHolderName(String name) {
        log.info("Searching accounts by holder name: {}", name);
        
        List<Account> accounts = accountRepository.findByAccountHolderNameContainingIgnoreCase(name);
        return accounts.stream()
                .map(this::mapToAccountResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    public TransactionResponse deposit(TransactionRequest request) {
        log.info("Processing deposit of {} for account: {}", request.getAmount(), request.getAccountNumber());
        
        validateAmount(request.getAmount());
        
        Account account = accountRepository.findActiveAccountByAccountNumber(hasher.hash(request.getAccountNumber()))
                .orElseThrow(() -> new AccountNotFoundException("Account with number " + request.getAccountNumber() + " not found"));
        
        if (account.getStatus() != Account.AccountStatus.ACTIVE) {
            throw new InactiveAccountException("Cannot perform transaction on inactive account");
        }
        
        BigDecimal newBalance = account.getBalance().add(request.getAmount());
        account.setBalance(newBalance);
        accountRepository.save(account);
        
        log.info("Deposit successful. New balance: {}", newBalance);
        
        return TransactionResponse.builder()
                .accountNumber(account.getAccountNumber())
                .transactionType("DEPOSIT")
                .amount(request.getAmount())
                .newBalance(newBalance)
                .description(request.getDescription())
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    @Override
    public TransactionResponse withdraw(TransactionRequest request) {
        log.info("Processing withdrawal of {} for account: {}", request.getAmount(), request.getAccountNumber());
        
        validateAmount(request.getAmount());
        
        Account account = accountRepository.findActiveAccountByAccountNumber(hasher.hash(request.getAccountNumber()))
                .orElseThrow(() -> new AccountNotFoundException("Account with number " + request.getAccountNumber() + " not found"));
        
        if (account.getStatus() != Account.AccountStatus.ACTIVE) {
            throw new InactiveAccountException("Cannot perform transaction on inactive account");
        }
        
        if (account.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientFundsException(
                    "Insufficient funds. Current balance: " + account.getBalance() + ", Requested amount: " + request.getAmount(),
                    account.getBalance(),
                    request.getAmount()
            );
        }
        
        BigDecimal newBalance = account.getBalance().subtract(request.getAmount());
        account.setBalance(newBalance);
        accountRepository.save(account);
        
        log.info("Withdrawal successful. New balance: {}", newBalance);
        
        return TransactionResponse.builder()
                .accountNumber(account.getAccountNumber())
                .transactionType("WITHDRAWAL")
                .amount(request.getAmount())
                .newBalance(newBalance)
                .description(request.getDescription())
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    @Override
    public void deleteAccount(String accountNumber) {
        log.info("Deleting account: {}", accountNumber);
        
        Account account = accountRepository.findByAccountNumberHash(hasher.hash(accountNumber))
                .orElseThrow(() -> new AccountNotFoundException("Account with number " + accountNumber + " not found"));
        
        if (account.getStatus() == Account.AccountStatus.INACTIVE) {
            throw new InactiveAccountException("Account is already inactive");
        }
        
        // Soft delete by setting status to INACTIVE
        account.setStatus(Account.AccountStatus.INACTIVE);
        accountRepository.save(account);
        
        log.info("Account {} deleted successfully", accountNumber);
    }
    
    @Override
    public AccountResponse updateAccountStatus(String accountNumber, Account.AccountStatus status) {
        log.info("Updating account {} status to {}", accountNumber, status);
        
        Account account = accountRepository.findByAccountNumberHash(hasher.hash(accountNumber))
                .orElseThrow(() -> new AccountNotFoundException("Account with number " + accountNumber + " not found"));
        
        account.setStatus(status);
        Account updatedAccount = accountRepository.save(account);
        
        return mapToAccountResponse(updatedAccount);
    }
    
    private String generateAccountNumber() {
        String accountNumber;
        String accountNumberHash;
        do {
            // Generate a 10-digit account number
            accountNumber = String.format("%010d", Math.abs(UUID.randomUUID().hashCode()) % 10000000000L);
            accountNumberHash = hasher.hash(accountNumber);
        } while (accountRepository.existsByAccountNumberHash(accountNumberHash));
        
        return accountNumber;
    }
    
    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Amount must be greater than zero");
        }
    }
    
    private AccountResponse mapToAccountResponse(Account account) {
        return AccountResponse.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .accountHolderName(account.getAccountHolderName())
                .email(account.getEmail())
                .phoneNumber(account.getPhoneNumber())
                .balance(account.getBalance())
                .accountType(account.getAccountType())
                .status(account.getStatus())
                .createdAt(account.getCreatedAt())
                .updatedAt(account.getUpdatedAt())
                .build();
    }
}
