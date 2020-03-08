package com.debski.accountservice.services;

import com.debski.accountservice.entities.Account;
import com.debski.accountservice.models.AccountDTO;
import com.debski.accountservice.repositories.AccountRepository;
import com.debski.accountservice.utils.TransformationUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Locale;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceUnitTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private MessageSource messageSource ;

    @Spy
    private TransformationUtils transformationUtils = new TransformationUtils();

    @InjectMocks
    private AccountServiceImpl accountServiceImpl;

    private AccountDTO accountDto;

    @Before
    public void setUp() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        accountDto = AccountDTO.builder().rawPassword("Password1").username("user").email("xyz@gmail.com").build();
        Mockito.when(accountRepository.save(Mockito.any(Account.class))).thenReturn(Account.builder().username("user").password("Password1").email("xyz@gmail.com").build());
    }

    @Test
    public void shouldMapDtoToEntity() {
        //when
        accountServiceImpl.save(accountDto);
        //then
        Mockito.verify(transformationUtils).dtoToEntity(accountDto);
    }

    @Test
    public void shouldInvokeSavingAccountEntity() {
        //when
        accountServiceImpl.save(accountDto);
        //then
        Mockito.verify(accountRepository).save(Mockito.any(Account.class));
    }

    @Test
    public void shouldReturnTrueIfPasswordLengthShorterThan8Characters() {
        assertTrue(accountServiceImpl.isToWeak("pass123"));
    }

    @Test
    public void shouldReturnFalseIfPasswordIsStrongEnough() {
        assertFalse(accountServiceImpl.isToWeak("Password1"));
    }

    @Test
    public void shouldReturnTrueIfPasswordDoesNotContainAtLeast1UppercaseLetter() {
        assertTrue(accountServiceImpl.isToWeak("password1"));
    }

    @Test
    public void shouldReturnTrueIfPasswordDoesNotContainAtLeast1Digit() {
        assertTrue(accountServiceImpl.isToWeak("Password"));
    }

}
