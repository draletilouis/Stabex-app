package com.demo.banking_app.repository;

import com.demo.banking_app.entity.IdempotencyKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface IdempotencyKeyRepository extends JpaRepository<IdempotencyKey, Long> {
    
    Optional<IdempotencyKey> findByIdempotencyKey(String idempotencyKey);
    
    @Modifying
    @Query("DELETE FROM IdempotencyKey i WHERE i.expiresAt < :now")
    int deleteExpiredKeys(@Param("now") LocalDateTime now);
    
    @Query("SELECT i FROM IdempotencyKey i WHERE i.idempotencyKey = :key AND i.expiresAt > :now")
    Optional<IdempotencyKey> findValidByIdempotencyKey(@Param("key") String idempotencyKey, @Param("now") LocalDateTime now);
}
