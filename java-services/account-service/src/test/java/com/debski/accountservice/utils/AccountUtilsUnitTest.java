package com.debski.accountservice.utils;

import com.debski.accountservice.entities.Account;
import com.debski.accountservice.entities.enums.Currency;
import com.debski.accountservice.entities.enums.Role;
import com.debski.accountservice.models.AccountDTO;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AccountUtilsUnitTest {

    private AccountUtils accountUtils = new AccountUtils(new BCryptPasswordEncoder());


    @Test
    public void shouldMapEntityToDto() {
        //given
        Account entity = Account.builder().username("user").password("Password1").email("xyz@gmail.com").build();
        //when
        AccountDTO accountDTO = accountUtils.entityToDto(entity);
        //then
        assertThat(accountDTO.getUsername(), is(entity.getUsername()));
        assertThat(accountDTO.getRawPassword(), is(nullValue()));
        assertThat(accountDTO.getEmail(), is(entity.getEmail()));
    }

    @Test
    public void shouldReturnNull() {
        //when
        AccountDTO accountDTO = accountUtils.entityToDto(null);
        //then
        assertThat(accountDTO, is(nullValue()));
    }

    @Test
    public void shouldMapDtoToEntity() {
        //given
        AccountDTO dto = AccountDTO.builder().username("user").rawPassword("Password1").email("xyz@gmail.com").build();
        //when
        Account entity = accountUtils.dtoToEntity(dto);
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
    public void test() {
        List<Object> objects = Arrays.asList(Currency.EUR.getDeclaringClass().getEnumConstants());
        System.out.println(objects);
    }
}
