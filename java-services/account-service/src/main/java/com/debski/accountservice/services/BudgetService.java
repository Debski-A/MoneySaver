package com.debski.accountservice.services;

import com.debski.accountservice.entities.Income;
import com.debski.accountservice.entities.Outcome;
import com.debski.accountservice.models.BudgetDropdownValuesDTO;

public interface BudgetService {
    void saveAccountIncome(String username, Income income);
    void saveAccountOutcome(String username, Outcome outcome);
    BudgetDropdownValuesDTO getBudgetDropdownValues();
}
