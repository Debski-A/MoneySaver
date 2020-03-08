package com.debski.accountservice.controllers;

import com.debski.accountservice.models.*;
import com.debski.accountservice.services.AccountService;
import com.debski.accountservice.services.FinanceService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
public class AccountController {

    private AccountService accountService;
    private FinanceService financeService;

    public AccountController(AccountService accountService, FinanceService financeService) {
        this.accountService = accountService;
        this.financeService = financeService;
    }

    @PostMapping("/create")
    public void createAccount(@RequestBody AccountDTO accountDto) {
        accountService.save(accountDto);
    }

    @PreAuthorize("#oauth2.hasScope('server')")
    @GetMapping("/get/{username}")
    public AccountDTO getAccountByUsername(@PathVariable String username) {
        return accountService.findByUsername(username);
    }

    @PreAuthorize("#oauth2.hasScope('ui')")
    @GetMapping("/current/finances")
    public List<FinanceDescriptionDTO> getFinances(Principal principal, @RequestParam(required = false) Integer startIndex, @RequestParam(required = false) Integer endIndex) {
        return financeService.findByAscendingDateInRange(principal.getName(), startIndex, endIndex);
    }

    @PreAuthorize("#oauth2.hasScope('ui')")
    @DeleteMapping("/current/finance/delete")
    public void deleteFinance(@RequestBody FinanceDescriptionDTO finance) {
        financeService.deleteFinance(finance.getFinanceType(), finance.getUuid());
    }


    @PreAuthorize("#oauth2.hasScope('ui')")
    @PutMapping("/current/add/income")
    public void addIncome(Principal principal, @RequestBody IncomeDTO incomeDTO) {
        incomeDTO.setOwner(principal.getName());
        accountService.addIncome(incomeDTO);
    }

    @PreAuthorize("#oauth2.hasScope('ui')")
    @PutMapping("/current/add/outcome")
    public void addOutcome(Principal principal, @RequestBody OutcomeDTO outcomeDTO) {
        outcomeDTO.setOwner(principal.getName());
        accountService.addOutcome(outcomeDTO);
    }

    @PreAuthorize("#oauth2.hasScope('ui')")
    @GetMapping("/dropdown_values")
    public FinanceDropdownValuesDTO getDropdownValues() {
        return financeService.getDropdownValues();
    }
}
