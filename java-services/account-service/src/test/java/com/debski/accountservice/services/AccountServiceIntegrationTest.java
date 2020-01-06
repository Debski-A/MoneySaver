package com.debski.accountservice.services;

import com.debski.accountservice.entities.*;
import com.debski.accountservice.entities.enums.Currency;
import com.debski.accountservice.entities.enums.Frequency;
import com.debski.accountservice.entities.enums.IncomeCategory;
import com.debski.accountservice.entities.enums.OutcomeCategory;
import com.debski.accountservice.exceptions.AccountException;
import com.debski.accountservice.models.AccountDTO;
import com.debski.accountservice.models.DropdownValuesDTO;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class AccountServiceIntegrationTest {

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
    public void shouldPersistAccount() {
        //given
        AccountDTO accountBeforeSave = AccountDTO.builder().username("user").rawPassword("Password1").email("xyz@gmail.com").build();
        //when
        AccountDTO accountAfterSave = accountService.save(accountBeforeSave);
        //then
        assertThat(accountAfterSave.getUsername(), equalTo("user"));
    }

    @Test
    public void shouldReturnNullWhenAccountNotFound() {
        //when
        AccountDTO accountFromDB = accountService.findByUsername("user");
        //then
        assertThat(accountFromDB, is(nullValue()));
    }


    @Test
    public void shouldThrowAccountExceptionBecauseMandatoryParamsAreMissing() {
        //given
        AccountDTO dtoWithoutMandatoryParams = AccountDTO.builder().build();
        exception.expect(AccountException.class);
        exception.expectMessage("Please provide correct username, password and email");
        //when
        accountService.save(dtoWithoutMandatoryParams);
    }

    @Test
    public void shouldThrowAccountExceptionInPolishBecauseMandatoryParamsAreMissing() {
        //given
        LocaleContextHolder.setLocale(Locale.forLanguageTag("pl"));
        AccountDTO dtoWithoutMandatoryParams = AccountDTO.builder().build();
        exception.expect(AccountException.class);
        exception.expectMessage("Wprowadź poprawną nazwę użytkownika, hasło lub email");
        //when
        accountService.save(dtoWithoutMandatoryParams);
    }

    @Test
    public void shouldThrowAccountExceptionBecauseUsernameIsMissing() {
        //given
        AccountDTO dtoWithoutMandatoryParams = AccountDTO.builder().rawPassword("Password1").email("xyz@gmail.com").build();
        exception.expect(AccountException.class);
        exception.expectMessage("Please provide correct username, password and email");
        //when
        accountService.save(dtoWithoutMandatoryParams);
    }

    @Test
    public void shouldThrowAccountExceptionBecauseUsernameIsTaken() {
        //given
        AccountDTO dto1 = AccountDTO.builder().username("user").rawPassword("Password1").email("xyz@gmail.com").build();
        AccountDTO dto2 = AccountDTO.builder().username("user").rawPassword("Password1").email("abc@gmail.com").build();
        exception.expect(AccountException.class);
        exception.expectMessage("Username already taken");
        //when
        accountService.save(dto1);
        accountService.save(dto2);
    }

    @Test
    public void shouldThrowAccountExceptionBecausePasswordIsToWeak() {
        //given
        exception.expect(AccountException.class);
        exception.expectMessage("Password is too weak");
        //when
        accountService.save(AccountDTO.builder().username("user").rawPassword("Password").email("xyz@gmail.com").build());
    }

    @Test
    public void shouldThrowAccountExceptionInPolishBecausePasswordIsToWeak() {
        //given
        LocaleContextHolder.setLocale(Locale.forLanguageTag("pl"));
        exception.expect(AccountException.class);
        exception.expectMessage("Hasło jest za słabe");
        //when
        accountService.save(AccountDTO.builder().username("user").rawPassword("Password").email("xyz@gmail.com").build());
    }

    @Test
    public void shouldThrowAccountExceptionBecauseInvalidEmail() {
        //given
        LocaleContextHolder.setLocale(Locale.forLanguageTag("pl"));
        exception.expect(AccountException.class);
        exception.expectMessage("Wprowadź poprawny adres email");
        //when
        accountService.save(AccountDTO.builder().username("user").rawPassword("Password1").email("xyz@.com").build());
    }

    @Test
    public void shouldThrowExceptionIfFutureDate() {
        //given
        exception.expect(AccountException.class);
        exception.expectMessage("Date cannot be from future");
        Income income = Income.builder().amount(BigDecimal.valueOf(3000)).currency(Currency.EUR).dateOfIncome(LocalDate.of(2219, 3, 4)).incomeCategory(IncomeCategory.PAYMENT).frequency(Frequency.MONTHLY).build();
        AccountDTO accountBeforeSave = AccountDTO.builder().username("user").rawPassword("Password1").email("xyz@gmail.com").incomes(Set.of(income)).build();
        //when
        AccountDTO accountAfterSave = accountService.save(accountBeforeSave);
    }

    @Test
    public void shouldThrowExceptionIfUsernameIsEmpty() {
        //given
        LocaleContextHolder.setLocale(Locale.forLanguageTag("pl"));
        exception.expect(AccountException.class);
        exception.expectMessage("Wprowadź poprawną nazwę użytkownika, hasło lub email");
        AccountDTO accountBeforeSave = AccountDTO.builder().username("").rawPassword("Password1").email("xyz@gmail.com").build();
        //when
        AccountDTO accountAfterSave = accountService.save(accountBeforeSave);
    }

    @Test
    public void shouldPersistAccountWithIncomes() {
        //given
        Income income1 = Income.builder().amount(BigDecimal.valueOf(3000)).currency(Currency.GBP).dateOfIncome(LocalDate.of(2019, 3, 4)).incomeCategory(IncomeCategory.PAYMENT).frequency(Frequency.MONTHLY).build();
        Income income2 = Income.builder().amount(BigDecimal.valueOf(100)).currency(Currency.GBP).dateOfIncome(LocalDate.of(2019, 11, 11)).incomeCategory(IncomeCategory.GIFT).frequency(Frequency.ONCE).build();
        AccountDTO accountBeforeSave1 = AccountDTO.builder().username("user").rawPassword("Password1").email("xyz@gmail.com").incomes(Set.of(income1, income2)).build();
        //when
        AccountDTO accountAfterSave1 = accountService.save(accountBeforeSave1);
        //then
        assertThat(accountAfterSave1.getIncomes(), equalTo(Set.of(income1, income2)));
    }

    @Test
    public void shouldPersistAccountWithOutcome() {
        //given
        Outcome outcome = Outcome.builder()
                .amount(BigDecimal.valueOf(20))
                .currency(Currency.PLN)
                .dateOfOutcome(LocalDate.of(2018, 12, 12))
                .frequency(Frequency.ONCE)
                .outcomeCategory(OutcomeCategory.ALCOHOL)
                .build();
        AccountDTO accountBeforeSave = AccountDTO.builder().username("user").rawPassword("Password1").email("xyz@gmail.com").outcomes(Set.of(outcome)).build();
        //when
        AccountDTO accountAfterSave = accountService.save(accountBeforeSave);
        //then
        assertThat(accountAfterSave.getOutcomes(), equalTo(Set.of(outcome)));
    }

    @Test
    public void shouldReturnDropdownValuesForPolishLang() {
        //given
        LocaleContextHolder.setLocale(Locale.forLanguageTag("pl"));
        var currencies = List.of("PLN", "EUR", "USD", "GBP");
        var frequencies = List.of("Jednorazowo", "Codziennie", "Raz w miesiącu", "Co kwartał", "Raz w roku");
        var outcomeCategories = List.of("Opłata", "Jedzenie", "Alkohol", "Prezent", "Samochód", "Inne");
        var incomeCategories = List.of("Wpłata", "Prezent", "Benefit", "Inne");
        var expectedResult = DropdownValuesDTO.builder()
                .currencies(currencies)
                .frequencies(frequencies)
                .outcomeCategories(outcomeCategories)
                .incomeCategories(incomeCategories)
                .build();
        //when
        DropdownValuesDTO dropdownValuesDTO = accountService.provideValuesForDropdowns();
        //then
        assertThat(dropdownValuesDTO, equalTo(expectedResult));
    }

    @Test
    public void shouldReturnDropdownValuesForEnglishLang() {
        //given
        LocaleContextHolder.setLocale(Locale.forLanguageTag("en"));
        var currencies = List.of("PLN", "EUR", "USD", "GBP");
        var frequencies = List.of("Once", "Daily", "Monthly", "Quarterly", "Yearly");
        var outcomeCategories = List.of("Fee", "Food", "Alcohol", "Gift", "Car", "Other");
        var incomeCategories = List.of("Payment", "Gift", "Benefit", "Other");
        var expectedResult = DropdownValuesDTO.builder()
                .currencies(currencies)
                .frequencies(frequencies)
                .outcomeCategories(outcomeCategories)
                .incomeCategories(incomeCategories)
                .build();
        //when
        DropdownValuesDTO dropdownValuesDTO = accountService.provideValuesForDropdowns();
        //then
        assertThat(dropdownValuesDTO, equalTo(expectedResult));
    }

    @Test
    public void shouldUpdateAccountWithNewProvidedValues() {
        //given
        AccountDTO dataInDatabase = prepareDataInDatabase();
        AccountDTO dataForUpdate = singleIncomeInAccountDto();
        Income incomeForUpdate = dataForUpdate.getIncomes().iterator().next();
        //when
        accountService.update(dataForUpdate);
        //then
        AccountDTO accountEntity = accountService.findByUsername("miecio");
        //assert that contains data from update
        assertThat(accountEntity.getIncomes(), hasSize(2));
        assertThat(accountEntity.getIncomes(), hasItem(incomeForUpdate));
    }

    private AccountDTO prepareDataInDatabase() {
        Outcome outcome1 = Outcome.builder()
                .currency(Currency.PLN)
                .outcomeCategory(OutcomeCategory.ALCOHOL)
                .dateOfOutcome(LocalDate.of(2020, 01, 01))
                .amount(BigDecimal.valueOf(35))
                .frequency(Frequency.ONCE)
                .note("Kacyk po sylwestrze")
                .build();
        Income income1 = Income.builder()
                .currency(Currency.PLN)
                .incomeCategory(IncomeCategory.BENEFIT)
                .dateOfIncome(LocalDate.of(2020, 01, 02))
                .amount(BigDecimal.valueOf(500))
                .frequency(Frequency.MONTHLY)
                .note("500+")
                .build();
        AccountDTO dto = AccountDTO.builder()
                .username("miecio")
                .rawPassword(("Password1"))
                .email("miecio@wp.pl")
                .outcomes(new HashSet<>() {{add(outcome1);}})
                .incomes(new HashSet<>() {{add(income1);}})
                .build();
        accountService.save(dto);
        return dto;
    }

    private AccountDTO singleIncomeInAccountDto() {
        Income income2 = Income.builder()
                .currency(Currency.GBP)
                .incomeCategory(IncomeCategory.BENEFIT)
                .dateOfIncome(LocalDate.of(2020, 01, 02))
                .amount(BigDecimal.valueOf(300))
                .frequency(Frequency.YEARLY)
                .note("Coroczne pieniążki z okazju urodzinek od wujcia Andrzejka")
                .build();
        AccountDTO incomeFromFrontend = AccountDTO.builder()
                .incomes(new HashSet<>() {{add(income2);}})
                .username("miecio")
                .build();
        return incomeFromFrontend;
    }
}