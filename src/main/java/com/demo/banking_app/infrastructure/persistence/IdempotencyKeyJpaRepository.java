package com.demo.banking_app.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface IdempotencyKeyJpaRepository extends JpaRepository<IdempotencyKeyEntity, Long> {
    
    Optional<IdempotencyKeyEntity> findByIdempotencyKey(String idempotencyKey);
    
    @Modifying
    @Query("DELETE FROM IdempotencyKeyEntity i WHERE i.expiresAt < :now")
    int deleteExpiredKeys(@Param("now") LocalDateTime now);
    
    @Query("SELECT i FROM IdempotencyKeyEntity i WHERE i.idempotencyKey = :key AND i.expiresAt > :now")
    Optional<IdempotencyKeyEntity> findValidByIdempotencyKey(@Param("key") String idempotencyKey, @Param("now") LocalDateTime now);
}


