package com.debski.accountservice.services;

import com.debski.accountservice.exceptions.AccountException;
import com.debski.accountservice.models.AccountDTO;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

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
}
