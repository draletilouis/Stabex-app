package com.demo.banking_app.infrastructure.persistence;

import com.demo.banking_app.application.port.out.IdempotencyRepository;
import com.demo.banking_app.domain.model.IdempotencyKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class JpaIdempotencyRepository implements IdempotencyRepository {
    
    private final IdempotencyKeyJpaRepository jpaRepository;
    private final IdempotencyKeyMapper mapper;
    
    @Override
    public Optional<IdempotencyKey> findByIdempotencyKey(String key) {
        return jpaRepository.findByIdempotencyKey(key)
                .map(mapper::toDomain);
    }
    
    @Override
    public IdempotencyKey save(IdempotencyKey key) {
        IdempotencyKeyEntity entity = mapper.toEntity(key);
        IdempotencyKeyEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }
    
    @Override
    public void deleteExpiredKeys() {
        int deletedCount = jpaRepository.deleteExpiredKeys(java.time.LocalDateTime.now());
        log.info("Cleaned up {} expired idempotency keys", deletedCount);
    }
}




