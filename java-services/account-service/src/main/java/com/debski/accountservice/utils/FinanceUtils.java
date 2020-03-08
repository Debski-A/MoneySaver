package com.debski.accountservice.utils;

import com.debski.accountservice.entities.Income;
import com.debski.accountservice.entities.Outcome;
import com.debski.accountservice.enums.FinanceType;
import com.debski.accountservice.models.FinanceDescriptionDTO;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class FinanceUtils {

    private MessageSource messageSource;

    public FinanceUtils(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public List<FinanceDescriptionDTO> mergeIncomesAndOutcomesToFinanceList(Set<Income> incomes, Set<Outcome> outcomes) {
        Stream<FinanceDescriptionDTO> incomeFinances = incomes.stream().map(i -> {
            return FinanceDescriptionDTO.builder()
                    .amount(i.getAmount())
                    .currencyDescription(messageSource.getMessage(i.getCurrency().toString().toLowerCase(), null, LocaleContextHolder.getLocale()))
                    .categoryDescription(messageSource.getMessage(i.getIncomeCategory().toString().toLowerCase(), null, LocaleContextHolder.getLocale()))
                    .frequencyDescription(messageSource.getMessage(i.getFrequency().toString().toLowerCase(), null, LocaleContextHolder.getLocale()))
                    .date(i.getDateOfIncome())
                    .note(i.getNote())
                    .uuid(i.getUuid())
                    .financeTypeDescription(messageSource.getMessage(FinanceType.INCOME.toString().toLowerCase(), null, LocaleContextHolder.getLocale()))
                    .financeType(FinanceType.INCOME)
                    .build();
        });

        Stream<FinanceDescriptionDTO> outcomeFinances = outcomes.stream().map(o -> {
            return FinanceDescriptionDTO.builder()
                    .amount(o.getAmount())
                    .currencyDescription(messageSource.getMessage(o.getCurrency().toString().toLowerCase(), null, LocaleContextHolder.getLocale()))
                    .categoryDescription(messageSource.getMessage(o.getOutcomeCategory().toString().toLowerCase(), null, LocaleContextHolder.getLocale()))
                    .frequencyDescription(messageSource.getMessage(o.getFrequency().toString().toLowerCase(), null, LocaleContextHolder.getLocale()))
                    .date(o.getDateOfOutcome())
                    .note(o.getNote())
                    .uuid(o.getUuid())
                    .financeTypeDescription(messageSource.getMessage(FinanceType.OUTCOME.toString().toLowerCase(), null, LocaleContextHolder.getLocale()))
                    .financeType(FinanceType.OUTCOME)
                    .build();
        });
        List<FinanceDescriptionDTO> result = Stream.concat(incomeFinances, outcomeFinances).sorted().collect(Collectors.toList());
        return result;
    }

    public  <E extends Enum> Map<Integer, String> translateAllTypes(E enumSource) {
        int oridinal = 0;
        Map<Integer, String> translatedTypes = new HashMap<>();
        List<Object> listOfTypes = Arrays.asList(enumSource.getDeclaringClass().getEnumConstants());
        for (Object e : listOfTypes) {
            translatedTypes.put(oridinal++, messageSource.getMessage(e.toString().toLowerCase(), null, LocaleContextHolder.getLocale()));
        }
        return translatedTypes;
    }

    /**
     * @param startIndex included
     * @param endIndex   excluded
     */
    public List<FinanceDescriptionDTO> filterAccordingToIndexes(List<FinanceDescriptionDTO> finance, Integer startIndex, Integer endIndex) {
        if (startIndex == null) startIndex = 0;
        if (endIndex == null || endIndex > finance.size()) endIndex = finance.size();

        if (startIndex < finance.size() && startIndex < endIndex) {
            List<FinanceDescriptionDTO> filteredFinance = new ArrayList<>();
            for (int i = startIndex; i < endIndex; i++) {
                filteredFinance.add(finance.get(i));
            }
            return filteredFinance;
        } else {
            return Collections.EMPTY_LIST;
        }
    }
}
