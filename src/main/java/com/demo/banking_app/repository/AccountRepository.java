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
    
    Optional<Account> findByAccountNumber(String accountNumber);
    
    Optional<Account> findByEmail(String email);
    
    List<Account> findByAccountHolderNameContainingIgnoreCase(String name);
    
    List<Account> findByAccountType(Account.AccountType accountType);
    
    List<Account> findByStatus(Account.AccountStatus status);
    
    @Query("SELECT a FROM Account a WHERE a.accountNumber = :accountNumber AND a.status = 'ACTIVE'")
    Optional<Account> findActiveAccountByAccountNumber(@Param("accountNumber") String accountNumber);
    
    boolean existsByAccountNumber(String accountNumber);
    
    boolean existsByEmail(String email);
}
