package com.demo.banking_app.service;

import com.demo.banking_app.dto.*;
import com.demo.banking_app.entity.Account;
import com.demo.banking_app.entity.IdempotencyKey;
import com.demo.banking_app.exception.*;
import com.demo.banking_app.repository.AccountRepository;
import com.demo.banking_app.util.DeterministicHasher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
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
    private final IdempotencyService idempotencyService;
    
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
        log.info("Processing deposit of {} for account: {} with idempotency key: {}", 
                request.getAmount(), request.getAccountNumber(), request.getIdempotencyKey());
        
        validateAmount(request.getAmount());
        
        // Check for existing idempotency key
        String accountNumberHash = hasher.hash(request.getAccountNumber());
        var existingKey = idempotencyService.findValidKey(request.getIdempotencyKey());
        
        if (existingKey.isPresent()) {
            IdempotencyKey key = existingKey.get();
            if (key.getStatus() == IdempotencyKey.Status.COMPLETED) {
                // Return cached response
                log.info("Returning cached response for idempotency key: {}", request.getIdempotencyKey());
                return parseCachedResponse(key.getResponseData());
            } else if (key.getStatus() == IdempotencyKey.Status.FAILED) {
                throw new IdempotencyException("Previous request with this idempotency key failed: " + key.getResponseData());
            }
            // If PENDING, continue processing (might be a retry)
        } else {
            // Create new idempotency key
            idempotencyService.createKey(request.getIdempotencyKey(), 
                    IdempotencyKey.OperationType.DEPOSIT, 
                    accountNumberHash, 
                    request.getAmount());
        }
        
        try {
            Account account = accountRepository.findActiveAccountByAccountNumber(accountNumberHash)
                    .orElseThrow(() -> new AccountNotFoundException("Account with number " + request.getAccountNumber() + " not found"));
            
            if (account.getStatus() != Account.AccountStatus.ACTIVE) {
                throw new InactiveAccountException("Cannot perform transaction on inactive account");
            }
            
            BigDecimal newBalance = account.getBalance().add(request.getAmount());
            account.setBalance(newBalance);
            
            // Save with optimistic locking
            accountRepository.save(account);
            
            TransactionResponse response = TransactionResponse.builder()
                    .accountNumber(account.getAccountNumber())
                    .transactionType("DEPOSIT")
                    .amount(request.getAmount())
                    .newBalance(newBalance)
                    .description(request.getDescription())
                    .idempotencyKey(request.getIdempotencyKey())
                    .timestamp(LocalDateTime.now())
                    .build();
            
            // Mark idempotency key as completed
            idempotencyService.markCompleted(request.getIdempotencyKey(), serializeResponse(response));
            
            log.info("Deposit successful. New balance: {}", newBalance);
            return response;
            
        } catch (ObjectOptimisticLockingFailureException e) {
            idempotencyService.markFailed(request.getIdempotencyKey(), "Optimistic locking failure - account was modified concurrently");
            throw new OptimisticLockingException("Account was modified by another transaction. Please retry.", e);
        } catch (Exception e) {
            idempotencyService.markFailed(request.getIdempotencyKey(), e.getMessage());
            throw e;
        }
    }
    
    @Override
    public TransactionResponse withdraw(TransactionRequest request) {
        log.info("Processing withdrawal of {} for account: {} with idempotency key: {}", 
                request.getAmount(), request.getAccountNumber(), request.getIdempotencyKey());
        
        validateAmount(request.getAmount());
        
        // Check for existing idempotency key
        String accountNumberHash = hasher.hash(request.getAccountNumber());
        var existingKey = idempotencyService.findValidKey(request.getIdempotencyKey());
        
        if (existingKey.isPresent()) {
            IdempotencyKey key = existingKey.get();
            if (key.getStatus() == IdempotencyKey.Status.COMPLETED) {
                // Return cached response
                log.info("Returning cached response for idempotency key: {}", request.getIdempotencyKey());
                return parseCachedResponse(key.getResponseData());
            } else if (key.getStatus() == IdempotencyKey.Status.FAILED) {
                throw new IdempotencyException("Previous request with this idempotency key failed: " + key.getResponseData());
            }
            // If PENDING, continue processing (might be a retry)
        } else {
            // Create new idempotency key
            idempotencyService.createKey(request.getIdempotencyKey(), 
                    IdempotencyKey.OperationType.WITHDRAWAL, 
                    accountNumberHash, 
                    request.getAmount());
        }
        
        try {
            Account account = accountRepository.findActiveAccountByAccountNumber(accountNumberHash)
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
            
            // Save with optimistic locking
            accountRepository.save(account);
            
            TransactionResponse response = TransactionResponse.builder()
                    .accountNumber(account.getAccountNumber())
                    .transactionType("WITHDRAWAL")
                    .amount(request.getAmount())
                    .newBalance(newBalance)
                    .description(request.getDescription())
                    .idempotencyKey(request.getIdempotencyKey())
                    .timestamp(LocalDateTime.now())
                    .build();
            
            // Mark idempotency key as completed
            idempotencyService.markCompleted(request.getIdempotencyKey(), serializeResponse(response));
            
            log.info("Withdrawal successful. New balance: {}", newBalance);
            return response;
            
        } catch (ObjectOptimisticLockingFailureException e) {
            idempotencyService.markFailed(request.getIdempotencyKey(), "Optimistic locking failure - account was modified concurrently");
            throw new OptimisticLockingException("Account was modified by another transaction. Please retry.", e);
        } catch (Exception e) {
            idempotencyService.markFailed(request.getIdempotencyKey(), e.getMessage());
            throw e;
        }
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
    
    private String serializeResponse(TransactionResponse response) {
        // Simple JSON serialization for caching
        return String.format("{\"accountNumber\":\"%s\",\"transactionType\":\"%s\",\"amount\":%s,\"newBalance\":%s,\"description\":\"%s\",\"idempotencyKey\":\"%s\",\"timestamp\":\"%s\"}",
                response.getAccountNumber(),
                response.getTransactionType(),
                response.getAmount(),
                response.getNewBalance(),
                response.getDescription() != null ? response.getDescription() : "",
                response.getIdempotencyKey(),
                response.getTimestamp());
    }
    
    private TransactionResponse parseCachedResponse(String responseData) {
        // Simple JSON parsing for cached responses
        // In a real application, you'd use a proper JSON library like Jackson
        try {
            // This is a simplified implementation - in production, use proper JSON parsing
            String[] parts = responseData.replaceAll("[{}\"]", "").split(",");
            String accountNumber = null, transactionType = null, description = null, idempotencyKey = null, timestamp = null;
            BigDecimal amount = null, newBalance = null;
            
            for (String part : parts) {
                String[] keyValue = part.split(":");
                if (keyValue.length == 2) {
                    String key = keyValue[0].trim();
                    String value = keyValue[1].trim();
                    
                    switch (key) {
                        case "accountNumber" -> accountNumber = value;
                        case "transactionType" -> transactionType = value;
                        case "amount" -> amount = new BigDecimal(value);
                        case "newBalance" -> newBalance = new BigDecimal(value);
                        case "description" -> description = value.isEmpty() ? null : value;
                        case "idempotencyKey" -> idempotencyKey = value;
                        case "timestamp" -> timestamp = value;
                    }
                }
            }
            
            return TransactionResponse.builder()
                    .accountNumber(accountNumber)
                    .transactionType(transactionType)
                    .amount(amount)
                    .newBalance(newBalance)
                    .description(description)
                    .idempotencyKey(idempotencyKey)
                    .timestamp(LocalDateTime.parse(timestamp))
                    .build();
        } catch (Exception e) {
            log.error("Failed to parse cached response: {}", responseData, e);
            throw new RuntimeException("Failed to parse cached response", e);
        }
    }
}
