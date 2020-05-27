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
            case INCOME: {
                result = calculateIncomes(accountData.getIncomes(), input);
                break;
            }
            case OUTCOME: {
                result = calculateOutcomes(accountData.getOutcomes(), input);
                break;
            }
            case BOTH: {
                result =  calculateBoth(accountData.getIncomes(), accountData.getOutcomes(), input);
                break;
            }
        }
        return result;
    }
    //TODO adjustIncome/OutcomeCurrencies strzela do zew. API, moze warto najpierw przefiltrowac Sety po zakresie dat, a pozniej dopiero
    //robic exchange walut. Ewentualnie zrobic scheduler na exchange walut, zeby nie strzelac do zew API dla kazdego requestu
    private CalculationOutput calculateIncomes(Set<IncomeDTO> incomes, CalculationInput input) {
        budgetCalculator.adjustIncomesCurrencies(incomes, input.getCurrency());
        incomes = budgetCalculator.filterIncomesByCategory(incomes, input.getIncomeCategory());
        validateAfterCategoryFilteringThatSetIsNotEmpty(incomes);

        LocalDate endOfMonth = budgetCalculator.specifyEndOfMonth(input.getStartDate());
        LocalDate beginningOfMonth = budgetCalculator.specifyBeginningOfMonth(input.getStartDate());

        incomes = budgetCalculator.filterIncomesByDateTimePeriod(incomes, beginningOfMonth, endOfMonth);
        validateAfterPeriodFilteringThatSetIsNotEmpty(incomes);

        Set<VisualisationPoint> visualisationPoints = budgetCalculator.calculateIncomesVisualizationPoints(incomes);
        visualisationPoints = budgetCalculator.fillRestOfDaysOfMonthWithZeroAmountValue(visualisationPoints, beginningOfMonth, endOfMonth);

        CalculationOutput result = CalculationOutput.builder()
                .calculationType(CalculationType.INCOME)
                .incomeCategory(input.getIncomeCategory())
                .currency(input.getCurrency())
                .visualisationPoints(visualisationPoints)
                .build();
        return result;
    }

    private CalculationOutput calculateOutcomes(Set<OutcomeDTO> outcomes, CalculationInput input) {
        budgetCalculator.adjustOutcomesCurrencies(outcomes, input.getCurrency());
        outcomes = budgetCalculator.filterOutcomesByCategory(outcomes, input.getOutcomeCategory());
        validateAfterCategoryFilteringThatSetIsNotEmpty(outcomes);

        LocalDate endOfMonth = budgetCalculator.specifyEndOfMonth(input.getStartDate());
        LocalDate beginningOfMonth = budgetCalculator.specifyBeginningOfMonth(input.getStartDate());

        outcomes = budgetCalculator.filterOutcomesByDateTimePeriod(outcomes, beginningOfMonth, endOfMonth);
        validateAfterPeriodFilteringThatSetIsNotEmpty(outcomes);

        Set<VisualisationPoint> visualisationPoints = budgetCalculator.calculateOutcomesVisualizationPoints(outcomes);
        visualisationPoints = budgetCalculator.fillRestOfDaysOfMonthWithZeroAmountValue(visualisationPoints, beginningOfMonth, endOfMonth);

        CalculationOutput result = CalculationOutput.builder()
                .calculationType(CalculationType.OUTCOME)
                .outcomeCategory(input.getOutcomeCategory())
                .currency(input.getCurrency())
                .visualisationPoints(visualisationPoints)
                .build();
        return result;
    }

    private CalculationOutput calculateBoth(Set<IncomeDTO> incomes, Set<OutcomeDTO> outcomes, CalculationInput input) {
        budgetCalculator.adjustIncomesCurrencies(incomes, input.getCurrency());
        budgetCalculator.adjustOutcomesCurrencies(outcomes, input.getCurrency());

        LocalDate startDate = input.getStartDate();
        LocalDate endDate = input.getEndDate();
        incomes = budgetCalculator.filterIncomesByDateTimePeriod(incomes, startDate, endDate);
        outcomes = budgetCalculator.filterOutcomesByDateTimePeriod(outcomes, startDate, endDate);
        validateAfterPeriodFilteringThatBothSetsAreNotEmpty(incomes, outcomes);

//        incomes = budgetCalculator.convertIncomesFrequencies(incomes, endDate);
//        outcomes = budgetCalculator.convertOutcomesFrequencies(outcomes, endDate);

        CalculationOutput result = budgetCalculator.mergeIncomesAndOutcomes(incomes, outcomes, input.getCurrency());
        return result;
    }

    private <T> void validateAfterCategoryFilteringThatSetIsNotEmpty(Set<T> incomesOrOutcomes) {
        if (incomesOrOutcomes.isEmpty()) {
            throw new CalculationException(messageSource.getMessage("no.data.for.provided.category", null, LocaleContextHolder.getLocale()));
        }
    }

    private <T> void validateAfterPeriodFilteringThatSetIsNotEmpty(Set<T> incomesOrOutcomes) {
        if (incomesOrOutcomes.isEmpty()) {
            throw new CalculationException(messageSource.getMessage("no.data.for.provided.period", null, LocaleContextHolder.getLocale()));
        }
    }

    private void validateAfterPeriodFilteringThatBothSetsAreNotEmpty(Set<IncomeDTO> incomes, Set<OutcomeDTO> outcomes) {
        if (incomes.isEmpty() && outcomes.isEmpty()) {
            throw new CalculationException(messageSource.getMessage("no.data.for.provided.period", null, LocaleContextHolder.getLocale()));
        }
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
