package com.demo.banking_app.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountJpaRepository extends JpaRepository<AccountEntity, Long> {
    
    @Query("SELECT a FROM AccountEntity a WHERE a.accountNumberHash = :hash AND a.status = 'ACTIVE'")
    Optional<AccountEntity> findActiveAccountByAccountNumberHash(@Param("hash") String hash);
    
    @Query("SELECT a FROM AccountEntity a WHERE a.accountNumberHash = :hash")
    Optional<AccountEntity> findByAccountNumberHash(@Param("hash") String hash);
    
    @Query("SELECT a FROM AccountEntity a WHERE a.emailHash = :hash")
    Optional<AccountEntity> findByEmailHash(@Param("hash") String hash);
    
    @Query("SELECT a FROM AccountEntity a WHERE a.accountHolderName LIKE %:name%")
    List<AccountEntity> findByAccountHolderNameContaining(@Param("name") String name);
    
    @Query("SELECT COUNT(a) > 0 FROM AccountEntity a WHERE a.emailHash = :hash")
    boolean existsByEmailHash(@Param("hash") String hash);
    
    @Query("SELECT COUNT(a) > 0 FROM AccountEntity a WHERE a.accountNumberHash = :hash")
    boolean existsByAccountNumberHash(@Param("hash") String hash);
}


