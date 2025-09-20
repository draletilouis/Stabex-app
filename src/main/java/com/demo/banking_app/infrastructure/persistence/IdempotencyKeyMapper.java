package com.demo.banking_app.infrastructure.persistence;

import com.demo.banking_app.domain.model.IdempotencyKey;
import com.demo.banking_app.domain.model.Money;
import org.springframework.stereotype.Component;

@Component
public class IdempotencyKeyMapper {
    
    public IdempotencyKey toDomain(IdempotencyKeyEntity entity) {
        return IdempotencyKey.builder()
                .idempotencyKey(entity.getIdempotencyKey())
                .operationType(IdempotencyKey.OperationType.valueOf(entity.getOperationType().name()))
                .accountNumberHash(entity.getAccountNumberHash())
                .amount(entity.getAmount() != null ? Money.of(entity.getAmount()) : null)
                .status(IdempotencyKey.Status.valueOf(entity.getStatus().name()))
                .responseData(entity.getResponseData())
                .createdAt(entity.getCreatedAt())
                .expiresAt(entity.getExpiresAt())
                .build();
    }
    
    public IdempotencyKeyEntity toEntity(IdempotencyKey domain) {
        return IdempotencyKeyEntity.builder()
                .idempotencyKey(domain.getIdempotencyKey())
                .operationType(IdempotencyKeyEntity.OperationType.valueOf(domain.getOperationType().name()))
                .accountNumberHash(domain.getAccountNumberHash())
                .amount(domain.getAmount() != null ? domain.getAmount().getAmount() : null)
                .status(IdempotencyKeyEntity.Status.valueOf(domain.getStatus().name()))
                .responseData(domain.getResponseData())
                .createdAt(domain.getCreatedAt())
                .expiresAt(domain.getExpiresAt())
                .build();
    }
}

