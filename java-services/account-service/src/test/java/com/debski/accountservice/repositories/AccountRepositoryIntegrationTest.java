package com.debski.accountservice.repositories;

import com.debski.accountservice.entities.Account;
import com.debski.accountservice.entities.Income;
import com.debski.accountservice.entities.enums.Currency;
import com.debski.accountservice.entities.enums.Frequency;
import com.debski.accountservice.entities.enums.IncomeCategory;
import com.debski.accountservice.entities.enums.Role;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
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
    private IncomeRepository incomeRepository;

    @Autowired
    private OutcomeRepository outcomeRepository;

    @Test
    public void checkIsEqualAndHashcodeWorkingCorrectly_AndCheckRolesRelation() {
        //given
        Account accountEntity = Account.builder().username("user").password("Password1").email("xyz@gmail.com").role(Role.PREMIUM).build();
        assertThat(accountEntity.getId(), is(nullValue()));
        //when
        Account accountEntityFromDb = accountRepository.save(accountEntity);
        //then
        assertThat(accountEntityFromDb.getId(), is(notNullValue()));
        assertThat(accountEntity, equalTo(accountEntityFromDb));

        accountEntity.setUsername("differentName");
        assertThat(accountEntity, equalTo(accountEntityFromDb));
        assertThat(accountEntityFromDb.getRole(), equalTo(Role.PREMIUM));
    }

    @Test
    public void shouldSaveAccountWithIncomesAndDeleteAccountWithIncomes() {
        // SAVE PART
        //given
        Income income = Income.builder()
                .frequency(Frequency.ONCE)
                .incomeCategory(IncomeCategory.OTHER)
                .dateOfIncome(LocalDate.of(2012,12,12))
                .amount(BigDecimal.valueOf(2000))
                .currency(Currency.PLN)
                .build();
        Account accountEntity = Account.builder()
                .username("user")
                .password("Password1")
                .email("xyz@gmail.com")
                .role(Role.PREMIUM)
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
        Income incomeById = incomeRepository.findById(incomeId).get();
        assertThat(accountById, notNullValue());
        assertThat(incomeById, notNullValue());

        // DELETE PART
        //when
        accountRepository.delete(accountEntity);
        //then
        Account accountByIdAfterDelete = accountRepository.findById(accountId).orElse(null);
        Income incomeByIdAfterDelete = incomeRepository.findById(incomeId).orElse(null);
        assertThat(accountByIdAfterDelete, nullValue());
        assertThat(incomeByIdAfterDelete, nullValue());


    }
}
