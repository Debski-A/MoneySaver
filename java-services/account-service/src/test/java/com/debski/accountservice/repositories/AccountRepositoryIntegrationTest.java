package com.debski.accountservice.repositories;

import com.debski.accountservice.entities.Account;
import com.debski.accountservice.entities.Role;
import com.debski.accountservice.entities.RoleTypes;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class AccountRepositoryIntegrationTest {

    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void checkIsEqualAndHashcodeWorkingCorrectly() {
        //given
        Account accountEntity = Account.builder().username("user").password("Password1").email("xyz@gmail.com").roles(Collections.singleton(Role.getSpecificRole(RoleTypes.USER))).build();
        assertThat(accountEntity.getId(), is(nullValue()));
        //when
        Account accountEntityFromDb = accountRepository.save(accountEntity);
        //then
        assertThat(accountEntityFromDb.getId(), is(notNullValue()));
        assertThat(accountEntity, equalTo(accountEntityFromDb));

        accountEntity.setUsername("differentName");
        assertThat(accountEntity, equalTo(accountEntityFromDb));
    }
}
