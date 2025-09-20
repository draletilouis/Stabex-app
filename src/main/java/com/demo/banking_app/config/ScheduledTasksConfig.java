package com.demo.banking_app.config;

import com.demo.banking_app.application.port.out.IdempotencyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ScheduledTasksConfig {
    
    private final IdempotencyRepository idempotencyRepository;
    
    /**
     * Clean up expired idempotency keys every hour
     */
    @Scheduled(fixedRate = 3600000) // 1 hour in milliseconds
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void cleanupExpiredIdempotencyKeys() {
        log.debug("Starting cleanup of expired idempotency keys");
        try {
            idempotencyRepository.deleteExpiredKeys();
        } catch (Exception e) {
            log.error("Error during idempotency key cleanup", e);
        }
    }
}
