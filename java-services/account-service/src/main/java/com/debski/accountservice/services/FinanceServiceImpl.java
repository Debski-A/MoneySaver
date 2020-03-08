package com.debski.accountservice.services;

import com.debski.accountservice.entities.Account;
import com.debski.accountservice.enums.*;
import com.debski.accountservice.models.FinanceDescriptionDTO;
import com.debski.accountservice.models.FinanceDropdownValuesDTO;
import com.debski.accountservice.repositories.AccountRepository;
import com.debski.accountservice.repositories.IncomeRepository;
import com.debski.accountservice.repositories.OutcomeRepository;
import com.debski.accountservice.utils.FinanceUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;
@Service
@Slf4j
@Transactional
public class FinanceServiceImpl implements FinanceService {

    private AccountRepository accountRepository;
    private IncomeRepository incomeRepository;
    private OutcomeRepository outcomeRepository;
    private FinanceUtils financeUtils;

    public FinanceServiceImpl(AccountRepository accountRepository, IncomeRepository incomeRepository, OutcomeRepository outcomeRepository, FinanceUtils financeUtils) {
        this.accountRepository = accountRepository;
        this.incomeRepository = incomeRepository;
        this.outcomeRepository = outcomeRepository;
        this.financeUtils = financeUtils;
    }

    @Override
    public FinanceDropdownValuesDTO getDropdownValues() {
        FinanceDropdownValuesDTO result = FinanceDropdownValuesDTO.builder()
                .currencies(financeUtils.translateAllTypes(Currency.EUR))
                .frequencies(financeUtils.translateAllTypes(Frequency.DAILY))
                .incomeCategories(financeUtils.translateAllTypes(IncomeCategory.BENEFIT))
                .outcomeCategories(financeUtils.translateAllTypes(OutcomeCategory.ALCOHOL))
                .build();
        return result;
    }


    /**
     * @param startIndex included
     * @param endIndex   excluded
     */
    @Override
    public List<FinanceDescriptionDTO> findByAscendingDateInRange(String username, Integer startIndex, Integer endIndex) {
        Account accountEntity = accountRepository.findByUsername(username);
        List<FinanceDescriptionDTO> descriptions = financeUtils.mergeIncomesAndOutcomesToFinanceList(accountEntity.getIncomes(), accountEntity.getOutcomes());
        descriptions = financeUtils.filterAccordingToIndexes(descriptions, startIndex, endIndex);
        return descriptions;
    }

    @Override
    public void deleteFinance(FinanceType financeType, UUID uuid) {
        switch (financeType) {
            case INCOME: {
                incomeRepository.removeByUuid(uuid);
                break;
            }
            case OUTCOME: {
                outcomeRepository.removeByUuid(uuid);
                break;
            }
        }
    }

}
