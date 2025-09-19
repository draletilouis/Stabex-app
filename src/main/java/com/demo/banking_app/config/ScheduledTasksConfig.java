package com.demo.banking_app.config;

import com.demo.banking_app.service.IdempotencyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ScheduledTasksConfig {
    
    private final IdempotencyService idempotencyService;
    
    /**
     * Clean up expired idempotency keys every hour
     */
    @Scheduled(fixedRate = 3600000) // 1 hour in milliseconds
    public void cleanupExpiredIdempotencyKeys() {
        log.debug("Starting cleanup of expired idempotency keys");
        try {
            idempotencyService.cleanupExpiredKeys();
        } catch (Exception e) {
            log.error("Error during idempotency key cleanup", e);
        }
    }
}
