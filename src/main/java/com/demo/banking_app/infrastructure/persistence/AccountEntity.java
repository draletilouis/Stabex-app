package com.demo.banking_app.infrastructure.persistence;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "accounts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "account_number", nullable = false)
    private String accountNumber;
    
    @Column(name = "account_number_hash", nullable = false, unique = true)
    private String accountNumberHash;
    
    @Column(name = "account_holder_name", nullable = false)
    private String accountHolderName;
    
    @Column(name = "email", nullable = false)
    private String email;
    
    @Column(name = "phone_number")
    private String phoneNumber;
    
    @Column(name = "email_hash")
    private String emailHash;
    
    @Column(name = "phone_hash")
    private String phoneHash;
    
    @Column(name = "balance", nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false)
    private AccountType accountType;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AccountStatus status;
    
    // @Version - Temporarily disabled for testing
    @Column(name = "version", nullable = false)
    @Builder.Default
    private Long version = 0L;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    public enum AccountType {
        SAVINGS, CHECKING, BUSINESS
    }
    
    public enum AccountStatus {
        ACTIVE, INACTIVE, SUSPENDED
    }
}

