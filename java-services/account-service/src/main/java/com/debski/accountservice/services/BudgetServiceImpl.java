package com.debski.accountservice.services;

import com.debski.accountservice.entities.Account;
import com.debski.accountservice.entities.enums.*;
import com.debski.accountservice.entities.enums.Currency;
import com.debski.accountservice.models.BudgetDTO;
import com.debski.accountservice.models.DropdownValuesDTO;
import com.debski.accountservice.repositories.AccountRepository;
import com.debski.accountservice.repositories.IncomeRepository;
import com.debski.accountservice.repositories.OutcomeRepository;
import com.debski.accountservice.utils.AccountUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
@Service
@Slf4j
@Transactional
public class BudgetServiceImpl implements BudgetService {

    private AccountRepository accountRepository;
    private IncomeRepository incomeRepository;
    private OutcomeRepository outcomeRepository;
    private AccountUtils accountUtils;
    private MessageSource messageSource;

    public BudgetServiceImpl(AccountRepository accountRepository, IncomeRepository incomeRepository, OutcomeRepository outcomeRepository, AccountUtils accountUtils, MessageSource messageSource) {
        this.accountRepository = accountRepository;
        this.incomeRepository = incomeRepository;
        this.outcomeRepository = outcomeRepository;
        this.accountUtils = accountUtils;
        this.messageSource = messageSource;
    }

    @Override
    public DropdownValuesDTO provideValuesForDropdowns() {
        DropdownValuesDTO result = DropdownValuesDTO.builder()
                .currencies(translateAllTypes(Currency.EUR))
                .frequencies(translateAllTypes(Frequency.DAILY))
                .incomeCategories(translateAllTypes(IncomeCategory.BENEFIT))
                .outcomeCategories(translateAllTypes(OutcomeCategory.ALCOHOL))
                .build();
        return result;
    }

    private <E extends Enum> Map<Integer, String> translateAllTypes(E enumSource) {
        int oridinal = 0;
        Map<Integer, String> translatedTypes = new HashMap<>();
        List<Object> listOfTypes = Arrays.asList(enumSource.getDeclaringClass().getEnumConstants());
        for (Object e : listOfTypes) {
            translatedTypes.put(oridinal++, messageSource.getMessage(e.toString().toLowerCase(), null, LocaleContextHolder.getLocale()));
        }
        return translatedTypes;
    }

    @Override
    public List<BudgetDTO> findByAscendingDateInRange(String username, Integer startIndex, Integer endIndex) {
        Account accountEntity = accountRepository.findByUsername(username);
        List<BudgetDTO> budget = accountUtils.mergeIncomesAndOutcomesToBudgetList(accountEntity.getIncomes(), accountEntity.getOutcomes());
        List<BudgetDTO> filteredBudget = accountUtils.filterAccordingToIndexes(budget, startIndex, endIndex);
        return filteredBudget;
    }

    @Override
    public void deleteBudget(BudgetType budgetType, UUID uuid) {
        switch (budgetType) {
            case INCOME: {
                Long rowsDeleted = incomeRepository.removeByUuid(uuid);
                break;
            }
            case OUTCOME: {
                Long rowsDeleted = outcomeRepository.removeByUuid(uuid);
                break;
            }
        }
    }


}
