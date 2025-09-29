package com.demo.banking_app.infrastructure.persistence;

import com.demo.banking_app.application.port.out.AccountRepository;
import com.demo.banking_app.domain.exception.ConcurrentModificationException;
import com.demo.banking_app.domain.model.Account;
import com.demo.banking_app.domain.model.AccountId;
import com.demo.banking_app.domain.model.AccountNumber;
import com.demo.banking_app.domain.model.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaAccountRepository implements AccountRepository {
    
    private final AccountJpaRepository jpaRepository;
    private final AccountMapper mapper;
    
    @Override
    public Account save(Account account) {
        try {
            AccountEntity entity = mapper.toEntity(account);
            AccountEntity saved = jpaRepository.save(entity);
            return mapper.toDomain(saved);
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new ConcurrentModificationException("Account was modified concurrently", e);
        }
    }
    
    @Override
    public Optional<Account> findById(AccountId id) {
        return jpaRepository.findById(id.getValue())
                .map(mapper::toDomain);
    }
    
    @Override
    public Optional<Account> findByAccountNumber(AccountNumber accountNumber) {
        return jpaRepository.findByAccountNumberHash(accountNumber.getHash())
                .map(mapper::toDomain);
    }
    
    @Override
    public Optional<Account> findByEmail(Email email) {
        return jpaRepository.findByEmailHash(email.getHash())
                .map(mapper::toDomain);
    }
    
    @Override
    public List<Account> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }
    
    @Override
    public List<Account> findByHolderNameContaining(String name) {
        return jpaRepository.findByAccountHolderNameContaining(name).stream()
                .map(mapper::toDomain)
                .toList();
    }
    
    @Override
    public boolean existsByEmail(Email email) {
        return jpaRepository.existsByEmailHash(email.getHash());
    }
    
    @Override
    public boolean existsByAccountNumber(AccountNumber accountNumber) {
        return jpaRepository.existsByAccountNumberHash(accountNumber.getHash());
    }
}



