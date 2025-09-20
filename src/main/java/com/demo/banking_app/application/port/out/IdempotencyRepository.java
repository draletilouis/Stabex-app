package com.demo.banking_app.application.port.out;

import com.demo.banking_app.domain.model.IdempotencyKey;

import java.util.Optional;

public interface IdempotencyRepository {
    Optional<IdempotencyKey> findByIdempotencyKey(String key);
    IdempotencyKey save(IdempotencyKey key);
    void deleteExpiredKeys();
}

