-- Fix idempotency table by dropping and recreating it without foreign key constraint
DROP TABLE IF EXISTS idempotency_keys;

-- Recreate idempotency table without foreign key constraint
CREATE TABLE idempotency_keys (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    idempotency_key VARCHAR(255) NOT NULL UNIQUE,
    operation_type VARCHAR(50) NOT NULL,
    account_number_hash VARCHAR(255),
    amount DECIMAL(19,2),
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    response_data JSON,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP NOT NULL,
    
    -- Indexes for performance
    INDEX idx_idempotency_key (idempotency_key),
    INDEX idx_operation_type (operation_type),
    INDEX idx_account_number_hash (account_number_hash),
    INDEX idx_expires_at (expires_at)
    
    -- Note: Foreign key constraint removed to avoid compatibility issues
    -- The relationship is maintained at the application level
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
