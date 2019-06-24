package com.debski.accountservice.com.debski.accountservice.unit;

import com.debski.accountservice.entities.Account;
import com.debski.accountservice.models.AccountDTO;
import com.debski.accountservice.repositories.AccountRepository;
import com.debski.accountservice.services.AccountServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceUnitTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private AccountServiceImpl accountServiceImpl;

    private AccountDTO accountDto;

    @Before
    public void setUp() {
        accountDto = AccountDTO.builder().rawPassword("pass").username("user").build();
    }

    @Test
    public void shouldEncodeRawPassword() {
        //when
        accountServiceImpl.save(accountDto);
        //then
        Mockito.verify(encoder).encode("pass");
    }

    @Test
    public void shouldInvokeSavingAccountEntity() {
        //when
        accountServiceImpl.save(accountDto);
        //then
        Mockito.verify(accountRepository).save(Mockito.any(Account.class));
    }

    @Test
    public void shouldReturnTrueWhenAccountExists() {
        //given
        Mockito.when(accountRepository.existsByUsername("user")).thenReturn(true);
        //when
        boolean result = accountServiceImpl.existsByUsername("user");
        //then
        assertThat(result, is(true));
    }

    @Test
    public void shouldReturnFalseWhenAccountNotExists() {
        //given
        Mockito.when(accountRepository.existsByUsername("user")).thenReturn(false);
        //when
        boolean result = accountServiceImpl.existsByUsername("user");
        //then
        assertThat(result, is(false));
    }

}
