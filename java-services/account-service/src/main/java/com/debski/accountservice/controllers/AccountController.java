package com.debski.accountservice.controllers;

import com.debski.accountservice.models.*;
import com.debski.accountservice.services.AccountService;
import com.debski.accountservice.services.BudgetService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;

@RestController
public class AccountController {

    private AccountService accountService;
    private BudgetService budgetService;

    public AccountController(AccountService accountService, BudgetService budgetService) {
        this.accountService = accountService;
        this.budgetService = budgetService;
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
    @GetMapping("/current/budget")
    public List<BudgetDTO> getIncomesAndOutcomes(Principal principal, @RequestParam(required = false) Integer startIndex, @RequestParam(required = false) Integer endIndex) {
        return budgetService.findByAscendingDateInRange(principal.getName(), startIndex, endIndex);
    }

    @PreAuthorize("#oauth2.hasScope('ui')")
    @DeleteMapping("/current/budget/delete")
    public void deleteBudget(@RequestBody BudgetDTO budget) {
        budgetService.deleteBudget(budget.getBudgetType(), budget.getUuid());
    }


    @PreAuthorize("#oauth2.hasScope('ui')")
    @PutMapping("/current/update/income")
    public AccountDTO updateAccountIncome(Principal principal, @RequestBody IncomeDTO incomeDTO) {
        AccountDTO accountDto = AccountDTO.builder()
                .username(principal.getName())
                .incomes(new HashSet<>() {{
                    add(incomeDTO);
                }})
                .build();
        return accountService.update(accountDto);
    }

    @PreAuthorize("#oauth2.hasScope('ui')")
    @PutMapping("/current/update/outcome")
    public AccountDTO updateAccountOutcome(Principal principal, @RequestBody OutcomeDTO outcomeDTO) {
        AccountDTO accountDto = AccountDTO.builder()
                .username(principal.getName())
                .outcomes(new HashSet<>() {{
                    add(outcomeDTO);
                }})
                .build();
        return accountService.update(accountDto);
    }

    @PreAuthorize("#oauth2.hasScope('ui')")
    @GetMapping("/dropdown_values")
    public DropdownValuesDTO getValueForDropdowns() {
        return budgetService.provideValuesForDropdowns();
    }
}
