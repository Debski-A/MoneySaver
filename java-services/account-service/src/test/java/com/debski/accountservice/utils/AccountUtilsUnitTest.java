package com.debski.accountservice.utils;

import com.debski.accountservice.entities.Account;
import com.debski.accountservice.entities.Income;
import com.debski.accountservice.entities.Outcome;
import com.debski.accountservice.entities.enums.*;
import com.debski.accountservice.models.AccountDTO;
import com.debski.accountservice.models.BudgetDTO;
import org.junit.Test;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AccountUtilsUnitTest {

    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource rs = new ResourceBundleMessageSource();
        rs.setBasename("messages");
        rs.setDefaultEncoding("UTF-8");
        rs.setUseCodeAsDefaultMessage(true);
        return rs;
    }

    private AccountUtils accountUtils = new AccountUtils(new BCryptPasswordEncoder(), messageSource());


    @Test
    public void shouldMapEntityToDto() {
        //given
        Account entity = Account.builder().username("user").password("Password1").email("xyz@gmail.com").build();
        //when
        AccountDTO accountDTO = accountUtils.accountEntityToDto(entity);
        //then
        assertThat(accountDTO.getUsername(), is(entity.getUsername()));
        assertThat(accountDTO.getRawPassword(), is(nullValue()));
        assertThat(accountDTO.getEmail(), is(entity.getEmail()));
    }

    @Test
    public void shouldReturnNull() {
        //when
        AccountDTO accountDTO = accountUtils.accountEntityToDto(null);
        //then
        assertThat(accountDTO, is(nullValue()));
    }

    @Test
    public void shouldMapDtoToEntity() {
        //given
        AccountDTO dto = AccountDTO.builder().username("user").rawPassword("Password1").email("xyz@gmail.com").build();
        //when
        Account entity = accountUtils.accountDtoToEntity(dto);
        //then
        assertThat(entity.getUsername(), is(dto.getUsername()));
        assertThat(entity.getPassword(), is(not(dto.getRawPassword())));
        assertThat(entity.getEmail(), is(dto.getEmail()));
        assertThat(entity.getRole(), is(Role.USER));
    }

    @Test
    public void shouldReturnTrueIfPasswordLengthShorterThan8Characters() {
        assertTrue(accountUtils.isToWeak("pass123"));
    }

    @Test
    public void shouldReturnFalseIfPasswordIsStrongEnough() {
        assertFalse(accountUtils.isToWeak("Password1"));
    }

    @Test
    public void shouldReturnTrueIfPasswordDoesNotContainAtLeast1UppercaseLetter() {
        assertTrue(accountUtils.isToWeak("password1"));
    }

    @Test
    public void shouldReturnTrueIfPasswordDoesNotContainAtLeast1Digit() {
        assertTrue(accountUtils.isToWeak("Password"));
    }

    @Test
    public void shouldMergeIncomesAndOutcomesToBudgetList() {
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
        List<BudgetDTO> budget = accountUtils.mergeIncomesAndOutcomesToBudgetList(Set.of(income1, income2), Set.of(outcome1, outcome2));
        //then
        assertThat(budget, hasSize(4));
        assertThat(budget.get(0).getDate(), equalTo(outcome1.getDateOfOutcome()));
        assertThat(budget.get(3).getDate(), equalTo(outcome2.getDateOfOutcome()));
    }

    private List<BudgetDTO> prepareTestData() {
        BudgetDTO b1 = BudgetDTO.builder()
                .date(LocalDate.of(2020, 2, 2))
                .build();
        BudgetDTO b2 = BudgetDTO.builder()
                .date(LocalDate.of(2020, 2, 1))
                .build();
        BudgetDTO b3 = BudgetDTO.builder()
                .date(LocalDate.of(2020, 2, 2))
                .build();
        BudgetDTO b4 = BudgetDTO.builder()
                .date(LocalDate.of(2020, 1, 1))
                .build();
        BudgetDTO b5 = BudgetDTO.builder()
                .date(LocalDate.of(2020, 2, 4))
                .build();
        List<BudgetDTO> testData = Arrays.asList(b1,b2,b3,b4,b5);
        Collections.sort(testData);
        return testData;
    }
    @Test
    public void shouldReturnEmptyListBecauseStartIndexOutOfBound() {
        //given
        List<BudgetDTO> testData = prepareTestData();
        //when
        List<BudgetDTO> result = accountUtils.filterAccordingToIndexes(testData, 5, 6);
        //then
        assertThat(result, empty());
    }

    @Test
    public void shouldReturnEmptyListBecauseStartIndexSameAsEndIndex() {
        //given
        List<BudgetDTO> testData = prepareTestData();
        //when
        List<BudgetDTO> result = accountUtils.filterAccordingToIndexes(testData, 2, 2);
        //then
        assertThat(result, empty());
    }

    @Test
    public void shouldReturnEmptyListBecauseStartIndexGreaterThanEndIndex() {
        //given
        List<BudgetDTO> testData = prepareTestData();
        //when
        List<BudgetDTO> result = accountUtils.filterAccordingToIndexes(testData, 4, 3);
        //then
        assertThat(result, empty());
    }

    @Test
    public void shouldReturnLessDataThanSpecifiedBetweenIndexesBecauseEndIndexOutOfBound() {
        //given
        List<BudgetDTO> testData = prepareTestData();
        //when
        List<BudgetDTO> result = accountUtils.filterAccordingToIndexes(testData, 2, 20);
        //then
        assertThat(result, hasSize(3));
        assertThat(result.get(0).getDate(), equalTo(LocalDate.of(2020, 2, 2)));
        assertThat(result.get(1).getDate(), equalTo(LocalDate.of(2020, 2, 2)));
        assertThat(result.get(2).getDate(), equalTo(LocalDate.of(2020, 2, 4)));
    }

    @Test
    public void shouldReturnSingleItemList() {
        //given
        List<BudgetDTO> testData = prepareTestData();
        //when
        List<BudgetDTO> result = accountUtils.filterAccordingToIndexes(testData, 3, 4);
        //then
        assertThat(result, hasSize(1));
        assertThat(result.get(0).getDate(), equalTo(LocalDate.of(2020, 2, 2)));
    }

    @Test
    public void shouldShouldReturnListWhichCoversAllIndexes() {
        //given
        List<BudgetDTO> testData = prepareTestData();
        //when
        List<BudgetDTO> result = accountUtils.filterAccordingToIndexes(testData, 0, 5);
        //then
        assertThat(result, equalTo(testData));
    }

    @Test
    public void shouldReturnWholeInputListBecauseStartIndexIsNullAndEndIndexIsNull() {
        //given
        List<BudgetDTO> testData = prepareTestData();
        //when
        List<BudgetDTO> result2 = accountUtils.filterAccordingToIndexes(testData, null, null);
        //then
        assertThat(result2, equalTo(testData));
    }

    @Test
    public void shouldReturnWholeInputListBecauseStartIndexIsNullAndEndIndexIsOutOfBound() {
        //given
        List<BudgetDTO> testData = prepareTestData();
        //when
        List<BudgetDTO> result = accountUtils.filterAccordingToIndexes(testData, null, 6);
        //then
        assertThat(result, equalTo(testData));
    }

    @Test
    public void shouldReturnSomeDataBecauseStartIndexIsNullAndEndIndexIsLessThanListSize() {
        //given
        List<BudgetDTO> testData = prepareTestData();
        //when
        List<BudgetDTO> result2 = accountUtils.filterAccordingToIndexes(testData, null, 4);
        //then
        assertThat(result2, hasSize(4));
    }

    @Test
    public void shouldReturnSomeDataIfStartIndexIsOkButEndIndexIsNull() {
        // if endIndex is null, then assign list.size() value to it
        //given
        List<BudgetDTO> testData = prepareTestData();
        //when
        List<BudgetDTO> result = accountUtils.filterAccordingToIndexes(testData, 3, null);
        //then
        assertThat(result, hasSize(2));
    }

}
