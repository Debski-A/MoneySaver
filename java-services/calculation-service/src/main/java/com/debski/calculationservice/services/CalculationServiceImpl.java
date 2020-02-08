package com.debski.calculationservice.services;

import com.debski.calculationservice.clients.AccountServiceClient;
import com.debski.calculationservice.enums.CalculationType;
import com.debski.calculationservice.exceptions.CalculationException;
import com.debski.calculationservice.models.*;
import com.debski.calculationservice.utils.BudgetCalculator;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Set;

@Service
public class CalculationServiceImpl implements CalculationService {

    private AccountServiceClient accountServiceClient;

    private MessageSource messageSource;

    private BudgetCalculator budgetCalculator;

    public CalculationServiceImpl(AccountServiceClient accountServiceClient, MessageSource messageSource, BudgetCalculator budgetCalculator) {
        this.accountServiceClient = accountServiceClient;
        this.messageSource = messageSource;
        this.budgetCalculator = budgetCalculator;
    }

    @Override
    public CalculationOutput makeCalculations(CalculationInput input, String username) {
        AccountDTO accountData = accountServiceClient.getAccountData(username);
        validateAccount(accountData, input.getCalculationType());
        CalculationOutput result = null;
        switch (input.getCalculationType()) {
            case INCOME: result = calculateIncomes(accountData.getIncomes(), input);
            case OUTCOME: result = calculateOutcomes(accountData.getOutcomes(), input);
            case BOTH: result =  calculateBoth(accountData.getIncomes(), accountData.getOutcomes(), input);
        }
        return result;
    }

    private CalculationOutput calculateIncomes(Set<IncomeDTO> incomes, CalculationInput input) {
        return null;
    }

    private CalculationOutput calculateOutcomes(Set<OutcomeDTO> outcomes, CalculationInput input) {
        return null;
    }

    private CalculationOutput calculateBoth(Set<IncomeDTO> incomes, Set<OutcomeDTO> outcomes, CalculationInput input) {
        LocalDate startDate = input.getStartDate();
        LocalDate endDate = input.getEndDate();
        budgetCalculator.adjustIncomesCurrencies(incomes, input.getCurrency());
        budgetCalculator.adjustOutcomesCurrencies(outcomes, input.getCurrency());

        incomes = budgetCalculator.filterIncomesByDateTimePeriod(incomes, startDate, endDate);
        outcomes = budgetCalculator.filterOutcomesByDateTimePeriod(outcomes, startDate, endDate);

        incomes = budgetCalculator.convertIncomesFrequencies(incomes, endDate);
        outcomes = budgetCalculator.convertOutcomesFrequencies(outcomes, endDate);

        CalculationOutput result = budgetCalculator.mergeIncomesAndOutcomes(incomes, outcomes);
        return result;
    }

    private void validateAccount(AccountDTO accountData, CalculationType calculationType) {
        if (accountData == null) {
            throw new CalculationException(messageSource.getMessage("user.not.found", null, LocaleContextHolder.getLocale()));
        }
        switch (calculationType) {
            case INCOME: {
                if (accountData.getIncomes() == null || accountData.getIncomes().isEmpty()) {
                    throw new CalculationException(messageSource.getMessage("incomes.not.found", null, LocaleContextHolder.getLocale()));
                }
                break;
            }
            case OUTCOME: {
                if (accountData.getOutcomes() == null || accountData.getOutcomes().isEmpty()) {
                    throw new CalculationException(messageSource.getMessage("outcomes.not.found", null, LocaleContextHolder.getLocale()));
                }
            }
            case BOTH: {
                if ( (accountData.getIncomes() == null || accountData.getIncomes().isEmpty()) && (accountData.getOutcomes() == null || accountData.getOutcomes().isEmpty()) ) {
                    throw new CalculationException(messageSource.getMessage("outcomes.and.incomes.not.found", null, LocaleContextHolder.getLocale()));
                }
            }
        }
    }

}
