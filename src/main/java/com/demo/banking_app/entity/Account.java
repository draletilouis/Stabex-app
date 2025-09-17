package com.demo.banking_app.entity;

import jakarta.persistence.*;
import jakarta.persistence.Convert;
import com.demo.banking_app.util.EncryptedStringConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "accounts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Convert(converter = EncryptedStringConverter.class)
    @Column(name = "account_number", nullable = false)
    private String accountNumber;

    @Column(name = "account_number_hash", unique = true, nullable = false)
    private String accountNumberHash;
    
    @Column(name = "account_holder_name", nullable = false)
    private String accountHolderName;
    
    @Convert(converter = EncryptedStringConverter.class)
    @Column(name = "email", nullable = false)
    private String email;
    
    @Convert(converter = EncryptedStringConverter.class)
    @Column(name = "phone_number")
    private String phoneNumber;

    // Deterministic hashes for querying equality
    @Column(name = "email_hash")
    private String emailHash;

    @Column(name = "phone_hash")
    private String phoneHash;
    
    @Column(name = "balance", precision = 19, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal balance = BigDecimal.ZERO;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false)
    private AccountType accountType;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private AccountStatus status = AccountStatus.ACTIVE;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    public enum AccountType {
        SAVINGS, CHECKING, BUSINESS
    }
    
    public enum AccountStatus {
        ACTIVE, INACTIVE, SUSPENDED
    }
}
