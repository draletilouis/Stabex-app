package com.demo.banking_app.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class IdempotencyKey {
    private final String idempotencyKey;
    private final OperationType operationType;
    private final String accountNumberHash;
    private final Money amount;
    private final Status status;
    private final String responseData;
    private final LocalDateTime createdAt;
    private final LocalDateTime expiresAt;
    
    public enum OperationType {
        DEPOSIT, WITHDRAWAL, TRANSFER
    }
    
    public enum Status {
        PENDING, COMPLETED, FAILED
    }
    
    public static IdempotencyKey create(String key, OperationType operationType, String accountNumberHash, Money amount) {
        return IdempotencyKey.builder()
                .idempotencyKey(key)
                .operationType(operationType)
                .accountNumberHash(accountNumberHash)
                .amount(amount)
                .status(Status.PENDING)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusHours(24))
                .build();
    }
    
    public IdempotencyKey markCompleted() {
        return IdempotencyKey.builder()
                .idempotencyKey(this.idempotencyKey)
                .operationType(this.operationType)
                .accountNumberHash(this.accountNumberHash)
                .amount(this.amount)
                .status(Status.COMPLETED)
                .responseData(this.responseData)
                .createdAt(this.createdAt)
                .expiresAt(this.expiresAt)
                .build();
    }
    
    public IdempotencyKey markCompleted(String responseData) {
        return IdempotencyKey.builder()
                .idempotencyKey(this.idempotencyKey)
                .operationType(this.operationType)
                .accountNumberHash(this.accountNumberHash)
                .amount(this.amount)
                .status(Status.COMPLETED)
                .responseData(responseData)
                .createdAt(this.createdAt)
                .expiresAt(this.expiresAt)
                .build();
    }
    
    public IdempotencyKey markFailed(String errorMessage) {
        return IdempotencyKey.builder()
                .idempotencyKey(this.idempotencyKey)
                .operationType(this.operationType)
                .accountNumberHash(this.accountNumberHash)
                .amount(this.amount)
                .status(Status.FAILED)
                .responseData(errorMessage)
                .createdAt(this.createdAt)
                .expiresAt(this.expiresAt)
                .build();
    }
    
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }
}

