package com.debski.accountservice.services;

import java.util.List;

public interface BudgetCategoriesService {
    List<String> getIncomeCategories();
    List<String> getOutcomeCategories();
}
