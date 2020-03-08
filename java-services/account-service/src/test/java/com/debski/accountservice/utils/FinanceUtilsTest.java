package com.debski.accountservice.utils;

import com.debski.accountservice.entities.Income;
import com.debski.accountservice.entities.Outcome;
import com.debski.accountservice.enums.Currency;
import com.debski.accountservice.enums.Frequency;
import com.debski.accountservice.enums.IncomeCategory;
import com.debski.accountservice.enums.OutcomeCategory;
import com.debski.accountservice.models.FinanceDescriptionDTO;
import org.junit.Test;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.hasSize;

public class FinanceUtilsTest {

    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource rs = new ResourceBundleMessageSource();
        rs.setBasename("messages");
        rs.setDefaultEncoding("UTF-8");
        rs.setUseCodeAsDefaultMessage(true);
        return rs;
    }

    private FinanceUtils financeUtils = new FinanceUtils(messageSource());

    @Test
    public void shouldMergeIncomesAndOutcomesToFinanceList() {
        //given
        Income income1 = Income.builder()
                .currency(Currency.PLN)
                .incomeCategory(IncomeCategory.GIFT)
                .frequency(Frequency.QUARTERLY)
                .dateOfIncome(LocalDate.of(2020, 2, 2))
                .amount(BigDecimal.valueOf(1234))
                .build();
        Income income2 = Income.builder()
                .currency(Currency.PLN)
                .incomeCategory(IncomeCategory.GIFT)
                .frequency(Frequency.QUARTERLY)
                .dateOfIncome(LocalDate.of(2020, 2, 2))
                .amount(BigDecimal.valueOf(1234))
                .build();
        Outcome outcome1 = Outcome.builder()
                .currency(Currency.EUR)
                .outcomeCategory(OutcomeCategory.ALCOHOL)
                .note("notatka")
                .frequency(Frequency.YEARLY)
                .dateOfOutcome(LocalDate.of(2020, 2, 1))
                .amount(BigDecimal.valueOf(123))
                .build();
        Outcome outcome2 = Outcome.builder()
                .currency(Currency.USD)
                .outcomeCategory(OutcomeCategory.FEE)
                .frequency(Frequency.ONCE)
                .dateOfOutcome(LocalDate.of(2020, 2, 4))
                .amount(BigDecimal.valueOf(0))
                .build();
        //when
        List<FinanceDescriptionDTO> finance = financeUtils.mergeIncomesAndOutcomesToFinanceList(Set.of(income1, income2), Set.of(outcome1, outcome2));
        //then
        assertThat(finance, hasSize(4));
        assertThat(finance.get(0).getDate(), equalTo(outcome1.getDateOfOutcome()));
        assertThat(finance.get(3).getDate(), equalTo(outcome2.getDateOfOutcome()));
    }

    private List<FinanceDescriptionDTO> prepareTestData() {
        FinanceDescriptionDTO b1 = FinanceDescriptionDTO.builder()
                .date(LocalDate.of(2020, 2, 2))
                .build();
        FinanceDescriptionDTO b2 = FinanceDescriptionDTO.builder()
                .date(LocalDate.of(2020, 2, 1))
                .build();
        FinanceDescriptionDTO b3 = FinanceDescriptionDTO.builder()
                .date(LocalDate.of(2020, 2, 2))
                .build();
        FinanceDescriptionDTO b4 = FinanceDescriptionDTO.builder()
                .date(LocalDate.of(2020, 1, 1))
                .build();
        FinanceDescriptionDTO b5 = FinanceDescriptionDTO.builder()
                .date(LocalDate.of(2020, 2, 4))
                .build();
        List<FinanceDescriptionDTO> testData = Arrays.asList(b1,b2,b3,b4,b5);
        Collections.sort(testData);
        return testData;
    }
    @Test
    public void shouldReturnEmptyListBecauseStartIndexOutOfBound() {
        //given
        List<FinanceDescriptionDTO> testData = prepareTestData();
        //when
        List<FinanceDescriptionDTO> result = financeUtils.filterAccordingToIndexes(testData, 5, 6);
        //then
        assertThat(result, empty());
    }

    @Test
    public void shouldReturnEmptyListBecauseStartIndexSameAsEndIndex() {
        //given
        List<FinanceDescriptionDTO> testData = prepareTestData();
        //when
        List<FinanceDescriptionDTO> result = financeUtils.filterAccordingToIndexes(testData, 2, 2);
        //then
        assertThat(result, empty());
    }

    @Test
    public void shouldReturnEmptyListBecauseStartIndexGreaterThanEndIndex() {
        //given
        List<FinanceDescriptionDTO> testData = prepareTestData();
        //when
        List<FinanceDescriptionDTO> result = financeUtils.filterAccordingToIndexes(testData, 4, 3);
        //then
        assertThat(result, empty());
    }

    @Test
    public void shouldReturnLessDataThanSpecifiedBetweenIndexesBecauseEndIndexOutOfBound() {
        //given
        List<FinanceDescriptionDTO> testData = prepareTestData();
        //when
        List<FinanceDescriptionDTO> result = financeUtils.filterAccordingToIndexes(testData, 2, 20);
        //then
        assertThat(result, hasSize(3));
        assertThat(result.get(0).getDate(), equalTo(LocalDate.of(2020, 2, 2)));
        assertThat(result.get(1).getDate(), equalTo(LocalDate.of(2020, 2, 2)));
        assertThat(result.get(2).getDate(), equalTo(LocalDate.of(2020, 2, 4)));
    }

    @Test
    public void shouldReturnSingleItemList() {
        //given
        List<FinanceDescriptionDTO> testData = prepareTestData();
        //when
        List<FinanceDescriptionDTO> result = financeUtils.filterAccordingToIndexes(testData, 3, 4);
        //then
        assertThat(result, hasSize(1));
        assertThat(result.get(0).getDate(), equalTo(LocalDate.of(2020, 2, 2)));
    }

    @Test
    public void shouldShouldReturnListWhichCoversAllIndexes() {
        //given
        List<FinanceDescriptionDTO> testData = prepareTestData();
        //when
        List<FinanceDescriptionDTO> result = financeUtils.filterAccordingToIndexes(testData, 0, 5);
        //then
        assertThat(result, equalTo(testData));
    }

    @Test
    public void shouldReturnWholeInputListBecauseStartIndexIsNullAndEndIndexIsNull() {
        //given
        List<FinanceDescriptionDTO> testData = prepareTestData();
        //when
        List<FinanceDescriptionDTO> result2 = financeUtils.filterAccordingToIndexes(testData, null, null);
        //then
        assertThat(result2, equalTo(testData));
    }

    @Test
    public void shouldReturnWholeInputListBecauseStartIndexIsNullAndEndIndexIsOutOfBound() {
        //given
        List<FinanceDescriptionDTO> testData = prepareTestData();
        //when
        List<FinanceDescriptionDTO> result = financeUtils.filterAccordingToIndexes(testData, null, 6);
        //then
        assertThat(result, equalTo(testData));
    }

    @Test
    public void shouldReturnSomeDataBecauseStartIndexIsNullAndEndIndexIsLessThanListSize() {
        //given
        List<FinanceDescriptionDTO> testData = prepareTestData();
        //when
        List<FinanceDescriptionDTO> result2 = financeUtils.filterAccordingToIndexes(testData, null, 4);
        //then
        assertThat(result2, hasSize(4));
    }

    @Test
    public void shouldReturnSomeDataIfStartIndexIsOkButEndIndexIsNull() {
        // if endIndex is null, then assign list.size() value to it
        //given
        List<FinanceDescriptionDTO> testData = prepareTestData();
        //when
        List<FinanceDescriptionDTO> result = financeUtils.filterAccordingToIndexes(testData, 3, null);
        //then
        assertThat(result, hasSize(2));
    }
}
