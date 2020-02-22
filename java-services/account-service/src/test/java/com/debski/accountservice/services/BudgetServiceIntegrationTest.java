package com.debski.accountservice.services;


import com.debski.accountservice.entities.enums.BudgetType;
import com.debski.accountservice.entities.enums.Currency;
import com.debski.accountservice.entities.enums.Frequency;
import com.debski.accountservice.entities.enums.IncomeCategory;
import com.debski.accountservice.models.AccountDTO;
import com.debski.accountservice.models.DropdownValuesDTO;
import com.debski.accountservice.models.IncomeDTO;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest
@RunWith(SpringRunner.class)
public class BudgetServiceIntegrationTest {

    @Autowired
    private BudgetService budgetService;

    @Autowired
    private AccountService accountService;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() {
        //default locale for every test:
        LocaleContextHolder.setLocale(Locale.forLanguageTag("en"));
    }

    @Test
    public void shouldReturnDropdownValuesForPolishLang() {
        //given
        LocaleContextHolder.setLocale(Locale.forLanguageTag("pl"));
        var currencies = Map.of(0, "PLN", 1, "EUR", 2, "USD", 3, "GBP");
        var frequencies = Map.of(0, "Jednorazowo", 1, "Codziennie", 2, "Raz w miesiącu", 3,"Co kwartał", 4,"Raz w roku");
        var outcomeCategories = Map.of(0, "Opłata", 1, "Jedzenie", 2, "Alkohol", 3, "Prezent",4,  "Samochód", 5, "Inne");
        var incomeCategories = Map.of(0, "Wpłata", 1,  "Prezent", 2, "Benefit", 3, "Inne");
        var expectedResult = DropdownValuesDTO.builder()
                .currencies(currencies)
                .frequencies(frequencies)
                .outcomeCategories(outcomeCategories)
                .incomeCategories(incomeCategories)
                .build();
        //when
        DropdownValuesDTO dropdownValuesDTO = budgetService.provideValuesForDropdowns();
        //then
        assertThat(dropdownValuesDTO, equalTo(expectedResult));
    }

    @Test
    public void shouldReturnDropdownValuesForEnglishLang() {
        //given
        LocaleContextHolder.setLocale(Locale.forLanguageTag("en"));
        var currencies = Map.of(0, "PLN", 1, "EUR", 2, "USD", 3, "GBP");
        var frequencies = Map.of(0,"Once", 1, "Daily", 2, "Monthly",  3,"Quarterly", 4, "Yearly");
        var outcomeCategories = Map.of(0, "Fee", 1,  "Food", 2,  "Alcohol", 3, "Gift", 4, "Car", 5, "Other");
        var incomeCategories = Map.of(0, "Payment", 1,  "Gift", 2, "Benefit", 3, "Other");
        var expectedResult = DropdownValuesDTO.builder()
                .currencies(currencies)
                .frequencies(frequencies)
                .outcomeCategories(outcomeCategories)
                .incomeCategories(incomeCategories)
                .build();
        //when
        DropdownValuesDTO dropdownValuesDTO = budgetService.provideValuesForDropdowns();
        //then
        assertThat(dropdownValuesDTO, equalTo(expectedResult));
    }


    @Test
    public void shouldDeleteIncome() {
        //given
        IncomeDTO income = IncomeDTO.builder()
                .owner("miecio")
                .currency(Currency.PLN)
                .incomeCategory(IncomeCategory.OTHER)
                .dateOfIncome(LocalDate.of(2018, 5, 12))
                .frequency(Frequency.ONCE)
                .amount(BigDecimal.valueOf(666))
                .build();
        AccountDTO account = AccountDTO.builder()
                .username("miecio")
                .email("miecio@gmail.com")
                .rawPassword("PAssword1")
                .incomes(new HashSet<>() {{
                    add(income);
                }})
                .build();
        accountService.save(account);
        AccountDTO accountReturn = accountService.findByUsername("miecio");
        assertThat(accountReturn.getIncomes(), hasSize(1));
        UUID incomeUuid = accountReturn.getIncomes().iterator().next().getUuid();
        //when
        budgetService.deleteBudget(BudgetType.INCOME, incomeUuid);
        //then
        AccountDTO accountReturn2 = accountService.findByUsername("miecio");
        assertThat(accountReturn2.getIncomes(), hasSize(0));
    }

}
