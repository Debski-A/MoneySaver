package com.debski.accountservice.controllers;

import com.debski.accountservice.models.AccountDTO;
import com.debski.accountservice.services.AccountService;
import com.debski.accountservice.services.AccountServiceImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/account")
public class AccountController {

    private AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/create")
    public AccountDTO createAccount(AccountDTO accountDto) {
        accountService.save(accountDto);
        return null;
    }
}
