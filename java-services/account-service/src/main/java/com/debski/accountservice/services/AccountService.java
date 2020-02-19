package com.debski.accountservice.services;

import com.debski.accountservice.models.AccountDTO;
import com.debski.accountservice.models.BudgetDTO;
import com.debski.accountservice.models.DropdownValuesDTO;

import java.util.List;

public interface AccountService {

    AccountDTO update(AccountDTO accountDTO);
    AccountDTO save(AccountDTO accountDto);
    AccountDTO findByUsername(String username);
    DropdownValuesDTO provideValuesForDropdowns();
    List<BudgetDTO> findByAscendingDateInRange(String username, Integer startIndex, Integer endIndex);
}