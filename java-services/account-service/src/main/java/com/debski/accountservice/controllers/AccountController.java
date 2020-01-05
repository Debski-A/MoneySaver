package com.debski.accountservice.controllers;

import com.debski.accountservice.models.AccountDTO;
import com.debski.accountservice.services.AccountService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
public class AccountController {

    private AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/create")
    public AccountDTO createAccount(@RequestBody AccountDTO accountDto) {
        return accountService.save(accountDto);
    }

    @PreAuthorize("#oauth2.hasScope('server')")
    @GetMapping("/get/{username}")
    public AccountDTO getAccountByName(@PathVariable String username) {
        return accountService.findByUsername(username);
    }

    @PreAuthorize("#oauth2.hasScope('ui')")
    @PutMapping("/current/income/add")
    public void saveAccountIncome(Principal principal, @RequestBody AccountDTO accountDto) {
        System.out.println(principal.getName());
        //TODO
    }

    @PreAuthorize("#oauth2.hasScope('ui')")
    @PutMapping("/current/outcome/add")
    public void saveAccountOutcome(Principal principal, @RequestBody AccountDTO accountDto) {
        System.out.println(principal.getName());
        //TODO
    }

    //TODO bedzie jeszcze allCurrencies i allPeriods - zrobic DTO dla wszystkich 4 list i wykonywac 1 call zamiast 4
}
