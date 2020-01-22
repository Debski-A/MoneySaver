package com.debski.accountservice.services;

import com.debski.accountservice.entities.Account;
import com.debski.accountservice.models.AccountDTO;
import com.debski.accountservice.repositories.AccountRepository;
import com.debski.accountservice.utils.AccountUtils;
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

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.TemporalUnit;
import java.util.List;
import java.util.Locale;

import static java.time.temporal.ChronoUnit.DAYS;

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
        Mockito.verify(accountUtils).accountDtoToEntity(accountDto);
    }

    @Test
    public void shouldInvokeSavingAccountEntity() {
        //when
        accountServiceImpl.save(accountDto);
        //then
        Mockito.verify(accountRepository).save(Mockito.any(Account.class));
    }

    @Test
    public void test() {
        LocalDate now = LocalDate.now();
        LocalDate past = LocalDate.now().minusMonths(4).minusDays(12).minusYears(1).minusMonths(3);
        Period p1 = Period.between(now, past);
        Period p2 = Period.between(past, now);
        int p1Days = p1.getDays();
        int p1Months = p1.getMonths();

        int p2Days = p2.getDays();
        int p2Months = p2.getMonths();

        long daysBetween = DAYS.between(past, now);
        long dayss = daysBetween / 11;
        for (int i = 1; i < 12; i++) {
            System.out.println("" + i + ")" +past);
            past = past.plusDays(dayss);
        }
        System.out.println("" + 12 + ")" +now);
        List<TemporalUnit> units = p2.getUnits();
//        System.out.println("hmmm");
    }

}
