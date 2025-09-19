package com.demo.banking_app.service;

import com.demo.banking_app.entity.IdempotencyKey;
import com.demo.banking_app.repository.IdempotencyKeyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class IdempotencyService {
    
    private final IdempotencyKeyRepository idempotencyKeyRepository;
    
    private static final int IDEMPOTENCY_EXPIRY_HOURS = 24;
    
    /**
     * Check if an idempotency key exists and is valid
     */
    public Optional<IdempotencyKey> findValidKey(String idempotencyKey) {
        return idempotencyKeyRepository.findValidByIdempotencyKey(idempotencyKey, LocalDateTime.now());
    }
    
    /**
     * Create a new idempotency key for tracking
     */
    public IdempotencyKey createKey(String idempotencyKey, 
                                   IdempotencyKey.OperationType operationType,
                                   String accountNumberHash,
                                   java.math.BigDecimal amount) {
        
        IdempotencyKey key = IdempotencyKey.builder()
                .idempotencyKey(idempotencyKey)
                .operationType(operationType)
                .accountNumberHash(accountNumberHash)
                .amount(amount)
                .status(IdempotencyKey.Status.PENDING)
                .expiresAt(LocalDateTime.now().plusHours(IDEMPOTENCY_EXPIRY_HOURS))
                .build();
        
        return idempotencyKeyRepository.save(key);
    }
    
    /**
     * Mark an idempotency key as completed with response data
     */
    public void markCompleted(String idempotencyKey, String responseData) {
        idempotencyKeyRepository.findByIdempotencyKey(idempotencyKey)
                .ifPresent(key -> {
                    key.setStatus(IdempotencyKey.Status.COMPLETED);
                    key.setResponseData(responseData);
                    idempotencyKeyRepository.save(key);
                    log.debug("Marked idempotency key {} as completed", idempotencyKey);
                });
    }
    
    /**
     * Mark an idempotency key as failed
     */
    public void markFailed(String idempotencyKey, String errorMessage) {
        idempotencyKeyRepository.findByIdempotencyKey(idempotencyKey)
                .ifPresent(key -> {
                    key.setStatus(IdempotencyKey.Status.FAILED);
                    key.setResponseData(errorMessage);
                    idempotencyKeyRepository.save(key);
                    log.debug("Marked idempotency key {} as failed", idempotencyKey);
                });
    }
    
    /**
     * Clean up expired idempotency keys
     */
    @Transactional
    public void cleanupExpiredKeys() {
        int deletedCount = idempotencyKeyRepository.deleteExpiredKeys(LocalDateTime.now());
        log.info("Cleaned up {} expired idempotency keys", deletedCount);
    }
}
