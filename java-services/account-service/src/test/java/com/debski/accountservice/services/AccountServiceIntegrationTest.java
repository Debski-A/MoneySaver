package com.debski.accountservice.services;

import com.debski.accountservice.entities.*;
import com.debski.accountservice.exceptions.AccountException;
import com.debski.accountservice.models.AccountDTO;
import com.debski.accountservice.repositories.BudgetRepositoryImpl;
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
import java.util.Locale;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class AccountServiceIntegrationTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private BudgetRepositoryImpl budgetRepository;

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
        IncomeCategory payment = budgetRepository.getIncomeCategory(IncomeCategoryTypes.PAYMENT);
        Income income = Income.builder().amount(BigDecimal.valueOf(3000)).currency(Currency.EUR).dateOfIncome(LocalDate.of(2219, 3, 4)).incomeCategory(payment).frequency(Frequency.MONTHLY).build();
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
        IncomeCategory payment = budgetRepository.getIncomeCategory(IncomeCategoryTypes.PAYMENT);
        IncomeCategory gift = budgetRepository.getIncomeCategory(IncomeCategoryTypes.GIFT);
        Income income1 = Income.builder().amount(BigDecimal.valueOf(3000)).currency(Currency.GBP).dateOfIncome(LocalDate.of(2019, 3, 4)).incomeCategory(payment).frequency(Frequency.MONTHLY).build();
        Income income2 = Income.builder().amount(BigDecimal.valueOf(100)).currency(Currency.GBP).dateOfIncome(LocalDate.of(2019, 11, 11)).incomeCategory(gift).frequency(Frequency.ONCE).build();
        AccountDTO accountBeforeSave1 = AccountDTO.builder().username("user").rawPassword("Password1").email("xyz@gmail.com").incomes(Set.of(income1, income2)).build();
        //when
        AccountDTO accountAfterSave1 = accountService.save(accountBeforeSave1);
        //then
        assertThat(accountAfterSave1.getIncomes(), equalTo(Set.of(income1, income2)));
    }

    @Test
    public void shouldPersistAccountWithOutcome() {
        //given
        OutcomeCategory alcohol = budgetRepository.getOutcomeCategory(OutcomeCategoryTypes.ALCOHOL);
        Outcome outcome = Outcome.builder()
                .amount(BigDecimal.valueOf(20))
                .currency(Currency.PLN)
                .dateOfOutcome(LocalDate.of(2018, 12, 12))
                .frequency(Frequency.ONCE)
                .outcomeCategory(alcohol)
                .build();
        AccountDTO accountBeforeSave = AccountDTO.builder().username("user").rawPassword("Password1").email("xyz@gmail.com").outcomes(Set.of(outcome)).build();
        //when
        AccountDTO accountAfterSave = accountService.save(accountBeforeSave);
        //then
        assertThat(accountAfterSave.getOutcomes(), equalTo(Set.of(outcome)));
    }

}