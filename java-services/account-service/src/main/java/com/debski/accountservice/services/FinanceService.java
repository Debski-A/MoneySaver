package com.debski.accountservice.services;

import com.debski.accountservice.enums.FinanceType;
import com.debski.accountservice.models.FinanceDescriptionDTO;
import com.debski.accountservice.models.FinanceDropdownValuesDTO;

import java.util.List;
import java.util.UUID;

public interface FinanceService {

    FinanceDropdownValuesDTO getDropdownValues();
    List<FinanceDescriptionDTO> findByAscendingDateInRange(String username, Integer startIndex, Integer endIndex);
    void deleteFinance(FinanceType financeType, UUID uuid);
}
