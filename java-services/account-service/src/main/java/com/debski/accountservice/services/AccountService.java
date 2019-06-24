package com.debski.accountservice.services;

import com.debski.accountservice.models.AccountDTO;

public interface AccountService {

    void save(AccountDTO accountDto);
    AccountDTO findByUsername(String username);
    boolean existsByUsername(String username);
}
