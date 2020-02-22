package com.debski.accountservice.services;

import com.debski.accountservice.models.AccountDTO;

public interface AccountService {

    AccountDTO update(AccountDTO accountDTO);
    AccountDTO save(AccountDTO accountDto);
    AccountDTO findByUsername(String username);

}