package com.debski.accountservice.com.debski.accountservice.integration;

import com.debski.accountservice.models.AccountDTO;
import com.debski.accountservice.services.AccountService;
import com.debski.accountservice.services.AccountServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class AccountServiceIntegrationTest {

    @Autowired
    private AccountService accountService;

    @Test
    public void shouldPersistAccount() {
        //given
        AccountDTO accountBeforeSave = AccountDTO.builder().username("user").rawPassword("pass").email("xyz@gmail.com").build();
        //when
        accountService.save(accountBeforeSave);
        //then
        AccountDTO accountAfterSave = accountService.findByUsername("user");
        assertThat(accountAfterSave.getUsername(), equalTo("user"));
    }

    @Test
    public void shouldReturnNullWhenAccountNotFound() {
        //when
        AccountDTO accountFromDB = accountService.findByUsername("user");
        //then
        assertThat(accountFromDB, is(nullValue()));
    }
}
