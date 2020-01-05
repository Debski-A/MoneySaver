package com.debski.accountservice.controllers;

import com.debski.accountservice.models.AccountDTO;
import com.debski.accountservice.services.AccountService;
import com.debski.accountservice.services.BudgetCategoriesService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
public class AccountController {

    private AccountService accountService;
    private BudgetCategoriesService budgetCategoriesService;

    public AccountController(AccountService accountService, BudgetCategoriesService budgetCategoriesService) {
        this.accountService = accountService;
        this.budgetCategoriesService = budgetCategoriesService;
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

    @PreAuthorize("#oauth2.hasScope('ui')")
    @GetMapping("/income/all")
    public List<String> getIncomeCategories() {
        return budgetCategoriesService.getIncomeCategories();
    }

    @PreAuthorize("#oauth2.hasScope('ui')")
    @GetMapping("/outcome/all")
    public List<String> getOutcomeCategories() {
        return budgetCategoriesService.getOutcomeCategories();
    }

    //TODO bedzie jeszcze allCurrencies i allPeriods - zrobic DTO dla wszystkich 4 list i wykonywac 1 call zamiast 4
}
