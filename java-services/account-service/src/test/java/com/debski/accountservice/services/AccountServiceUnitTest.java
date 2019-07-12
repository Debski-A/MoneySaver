package com.debski.accountservice.services;

import com.debski.accountservice.entities.Account;
import com.debski.accountservice.models.AccountDTO;
import com.debski.accountservice.repositories.AccountRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Locale;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceUnitTest {

    @Mock
    private AccountRepository accountRepository;

    @Spy
    private AccountUtils accountUtils = new AccountUtils(new BCryptPasswordEncoder());

    @Mock
    private ResourceBundleMessageSource messageSource ;

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
        Mockito.verify(accountUtils).dtoToEntity(accountDto);
    }

    @Test
    public void shouldInvokeSavingAccountEntity() {
        //when
        accountServiceImpl.save(accountDto);
        //then
        Mockito.verify(accountRepository).save(Mockito.any(Account.class));
    }

}
