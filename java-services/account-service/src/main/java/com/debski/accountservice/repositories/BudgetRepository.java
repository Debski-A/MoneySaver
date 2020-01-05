package com.debski.accountservice.repositories;

import com.debski.accountservice.entities.IncomeCategory;
import com.debski.accountservice.entities.OutcomeCategory;

import java.util.List;

public interface BudgetRepository {

    List<IncomeCategory> getAllIncomeCategories();
    List<OutcomeCategory> getAllOutcomeCategories();
}
