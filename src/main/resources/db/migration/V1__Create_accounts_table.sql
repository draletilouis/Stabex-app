-- Create accounts table with unique constraints and optimistic locking
CREATE TABLE accounts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    account_number VARCHAR(255) NOT NULL,
    account_number_hash VARCHAR(255) NOT NULL UNIQUE,
    account_holder_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone_number VARCHAR(255),
    email_hash VARCHAR(255),
    phone_hash VARCHAR(255),
    balance DECIMAL(19,2) NOT NULL DEFAULT 0.00,
    account_type VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
    version BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    -- Unique constraints for business logic
    UNIQUE KEY uk_account_number_hash (account_number_hash),
    UNIQUE KEY uk_email_hash (email_hash),
    
    -- Indexes for performance
    INDEX idx_account_holder_name (account_holder_name),
    INDEX idx_status (status),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
