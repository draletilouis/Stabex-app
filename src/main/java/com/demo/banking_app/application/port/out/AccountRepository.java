package com.demo.banking_app.application.port.out;

import com.demo.banking_app.domain.model.Account;
import com.demo.banking_app.domain.model.AccountId;
import com.demo.banking_app.domain.model.AccountNumber;
import com.demo.banking_app.domain.model.Email;

import java.util.List;
import java.util.Optional;

public interface AccountRepository {
    Account save(Account account);
    Optional<Account> findById(AccountId id);
    Optional<Account> findByAccountNumber(AccountNumber accountNumber);
    Optional<Account> findByEmail(Email email);
    List<Account> findAll();
    List<Account> findByHolderNameContaining(String name);
    boolean existsByEmail(Email email);
    boolean existsByAccountNumber(AccountNumber accountNumber);
}

