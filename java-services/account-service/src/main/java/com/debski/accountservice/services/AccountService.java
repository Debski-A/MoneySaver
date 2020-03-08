package com.debski.accountservice.services;

import com.debski.accountservice.models.AccountDTO;
import com.debski.accountservice.models.IncomeDTO;
import com.debski.accountservice.models.OutcomeDTO;

public interface AccountService {

    void save(AccountDTO accountDto);
    AccountDTO findByUsername(String username);
    void addIncome(IncomeDTO income);
    void addOutcome(OutcomeDTO outcomeDTO);
}