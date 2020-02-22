package com.debski.accountservice.services;

import com.debski.accountservice.entities.enums.BudgetType;
import com.debski.accountservice.models.BudgetDTO;
import com.debski.accountservice.models.DropdownValuesDTO;

import java.util.List;
import java.util.UUID;

public interface BudgetService {

    DropdownValuesDTO provideValuesForDropdowns();
    List<BudgetDTO> findByAscendingDateInRange(String username, Integer startIndex, Integer endIndex);
    void deleteBudget(BudgetType budgetType, UUID uuid);
}
