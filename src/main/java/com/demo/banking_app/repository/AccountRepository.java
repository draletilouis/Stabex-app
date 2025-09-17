package com.demo.banking_app.repository;

import com.demo.banking_app.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    
    Optional<Account> findByAccountNumberHash(String accountNumberHash);
    
    // Hash-based equality queries for encrypted fields
    Optional<Account> findByEmailHash(String emailHash);
    
    List<Account> findByAccountHolderNameContainingIgnoreCase(String name);
    
    List<Account> findByAccountType(Account.AccountType accountType);
    
    List<Account> findByStatus(Account.AccountStatus status);
    
    @Query("SELECT a FROM Account a WHERE a.accountNumberHash = :accountNumberHash AND a.status = 'ACTIVE'")
    Optional<Account> findActiveAccountByAccountNumber(@Param("accountNumberHash") String accountNumberHash);
    
    boolean existsByAccountNumberHash(String accountNumberHash);
    
    boolean existsByEmailHash(String emailHash);
}
