package com.debski.accountservice.repositories;

import com.debski.accountservice.entities.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class AccountRepositoryIntegrationTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RoleRepositoryImpl roleRepository;

    @Autowired
    private BudgetRepositoryImpl budgetRepository;

    @Test
    public void checkIsEqualAndHashcodeWorkingCorrectly_AndCheckRolesRelation() {
        //given
        Account accountEntity = Account.builder().username("user").password("Password1").email("xyz@gmail.com").roles(Collections.singleton(roleRepository.getRole(RoleTypes.PREMIUM))).build();
        assertThat(accountEntity.getId(), is(nullValue()));
        //when
        Account accountEntityFromDb = accountRepository.save(accountEntity);
        //then
        assertThat(accountEntityFromDb.getId(), is(notNullValue()));
        assertThat(accountEntity, equalTo(accountEntityFromDb));

        accountEntity.setUsername("differentName");
        assertThat(accountEntity, equalTo(accountEntityFromDb));
        assertThat(accountEntityFromDb.getRoles(), contains(equalTo(new Role(RoleTypes.PREMIUM))));
    }

    @Test
    public void shouldSaveAccountWithIncomesAndDeleteAccountWithIncomes() {
        // SAVE PART
        //given
        IncomeCategory other = budgetRepository.getIncomeCategory(IncomeCategoryTypes.OTHER);
        Income income = Income.builder()
                .frequency(Frequency.ONCE)
                .incomeCategory(other)
                .dateOfIncome(LocalDate.of(2012,12,12))
                .amount(BigDecimal.valueOf(2000))
                .currency(Currency.PLN)
                .build();
        Account accountEntity = Account.builder()
                .username("user")
                .password("Password1")
                .email("xyz@gmail.com")
                .roles(Collections.singleton(roleRepository.getRole(RoleTypes.PREMIUM)))
                .incomes(Set.of(income))
                .build();
        income.setAccount(accountEntity);
        //when
        Account accountEntityFromDb = accountRepository.save(accountEntity);
        //then
        assertThat(accountEntityFromDb.getIncomes().iterator().next(), equalTo(income));
        assertThat(accountEntity, equalTo(accountEntityFromDb));
        Long accountId = accountEntityFromDb.getId();
        Long incomeId = accountEntityFromDb.getIncomes().iterator().next().getId();
        Account accountById = accountRepository.findById(accountId).orElse(null);
        Income incomeById = budgetRepository.findIncomeById(incomeId);
        assertThat(accountById, notNullValue());
        assertThat(incomeById, notNullValue());

        // DELETE PART
        //when
        accountRepository.delete(accountEntity);
        //then
        Account accountByIdAfterDelete = accountRepository.findById(accountId).orElse(null);
        Income incomeByIdAfterDelete = budgetRepository.findIncomeById(incomeId);
        assertThat(accountByIdAfterDelete, nullValue());
        assertThat(incomeByIdAfterDelete, nullValue());


    }
}
