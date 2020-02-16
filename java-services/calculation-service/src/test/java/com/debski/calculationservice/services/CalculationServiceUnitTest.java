package com.debski.calculationservice.services;

import com.debski.calculationservice.clients.AccountServiceClient;
import com.debski.calculationservice.clients.ExchangeRatesClient;
import com.debski.calculationservice.clients.FakeAccountServiceClient;
import com.debski.calculationservice.enums.CalculationType;
import com.debski.calculationservice.enums.Currency;
import com.debski.calculationservice.exceptions.CalculationException;
import com.debski.calculationservice.models.AccountDTO;
import com.debski.calculationservice.models.CalculationInput;
import com.debski.calculationservice.models.CalculationOutput;
import com.debski.calculationservice.models.ExchangeRatesContainer;
import com.debski.calculationservice.utils.BudgetCalculator;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CalculationServiceUnitTest {

    @Spy
    private AccountServiceClient fakeAccountServiceClient = new FakeAccountServiceClient();

    @Mock
    private ExchangeRatesClient fakeExchangeRatesClient;

    private MessageSource messageSource() {
        ResourceBundleMessageSource rs = new ResourceBundleMessageSource();
        rs.setBasename("messages");
        rs.setDefaultEncoding("UTF-8");
        rs.setUseCodeAsDefaultMessage(true);
        return rs;
    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private BudgetCalculator budgetCalculator;

    private CalculationService calculationService;

    @Before
    public void setUp() {
        this.budgetCalculator = new BudgetCalculator(fakeExchangeRatesClient, messageSource());
        this.calculationService = new CalculationServiceImpl(fakeAccountServiceClient, messageSource(), budgetCalculator);
    }

    @Test
    public void shouldNotThrowExceptionAndPassAllAsserts() {
        //given
        ExchangeRatesContainer containerGBP = new ExchangeRatesContainer();
        containerGBP.setBase(Currency.GBP);
        containerGBP.setRates(Map.of(Currency.PLN.name(), BigDecimal.valueOf(5.23343533457)));
        when(fakeExchangeRatesClient.getRates(Currency.GBP)).thenReturn(containerGBP);

        ExchangeRatesContainer containerEUR = new ExchangeRatesContainer();
        containerEUR.setBase(Currency.EUR);
        containerEUR.setRates(Map.of(Currency.PLN.name(), BigDecimal.valueOf(4.33432434390)));
        when(fakeExchangeRatesClient.getRates(Currency.EUR)).thenReturn(containerEUR);

        ExchangeRatesContainer containerUSD = new ExchangeRatesContainer();
        containerUSD.setBase(Currency.USD);
        containerUSD.setRates(Map.of(Currency.PLN.name(), BigDecimal.valueOf(4.233423324234)));
        when(fakeExchangeRatesClient.getRates(Currency.USD)).thenReturn(containerUSD);

        CalculationInput input = CalculationInput.builder()
                .calculationType(CalculationType.BOTH)
                .currency(Currency.PLN)
                .startDate(LocalDate.of(2019, 1, 1))
                .endDate(LocalDate.of(2020, 1, 1))
                .build();
        //when
        CalculationOutput calculationOutput = calculationService.makeCalculations(input, "miecio");
        //then
        assertThat(calculationOutput.getCalculationType(), equalTo(CalculationType.BOTH));
        assertThat(calculationOutput.getCurrency(), equalTo(Currency.PLN));
        assertThat(calculationOutput.getIncomeCategory(), nullValue());
        assertThat(calculationOutput.getOutcomeCategory(), nullValue());
    }

    @Test
    public void shouldThrowExceptionBecauseIncomesAndOutcomesAfterFilteringAreEmpty() {
        //given
        LocaleContextHolder.setLocale(Locale.UK);
        ExchangeRatesContainer containerGBP = new ExchangeRatesContainer();
        containerGBP.setBase(Currency.GBP);
        containerGBP.setRates(Map.of(Currency.PLN.name(), BigDecimal.valueOf(5.23343533457)));
        when(fakeExchangeRatesClient.getRates(Currency.GBP)).thenReturn(containerGBP);

        ExchangeRatesContainer containerEUR = new ExchangeRatesContainer();
        containerEUR.setBase(Currency.EUR);
        containerEUR.setRates(Map.of(Currency.PLN.name(), BigDecimal.valueOf(4.33432434390)));
        when(fakeExchangeRatesClient.getRates(Currency.EUR)).thenReturn(containerEUR);

        ExchangeRatesContainer containerUSD = new ExchangeRatesContainer();
        containerUSD.setBase(Currency.USD);
        containerUSD.setRates(Map.of(Currency.PLN.name(), BigDecimal.valueOf(4.233423324234)));
        when(fakeExchangeRatesClient.getRates(Currency.USD)).thenReturn(containerUSD);

        CalculationInput input = CalculationInput.builder()
                .calculationType(CalculationType.BOTH)
                .currency(Currency.PLN)
                .startDate(LocalDate.of(2016, 1, 1))
                .endDate(LocalDate.of(2017, 1, 1))
                .build();
        expectedException.expect(CalculationException.class);
        expectedException.expectMessage("There are no incomes and outcomes for provided time period");
        //when
        CalculationOutput calculationOutput = calculationService.makeCalculations(input, "miecio");
        //then expect Exception
    }

    @Test(expected = CalculationException.class)
    public void shouldThrowExceptionWhenAccountIsNull() {
        //given
        when(fakeAccountServiceClient.getAccountData("miecio")).thenReturn(null);
        //when
        calculationService.makeCalculations(CalculationInput.builder().build(), "miecio");
        //then expect Exception
    }

    @Test(expected = CalculationException.class)
    public void shouldThrowExceptionWhenRatesIsNull() {
        //given
        CalculationInput.builder().currency(Currency.EUR).build();
        //when
        calculationService.makeCalculations(CalculationInput.builder().calculationType(CalculationType.BOTH).build(), "miecio");
        //then expect Exception
    }

    @Test
    public void shouldThrowExceptionWithNoIncomesMessage() {
        //given
        LocaleContextHolder.setLocale(Locale.forLanguageTag("pl"));
        expectedException.expect(CalculationException.class);
        expectedException.expectMessage("To konto nie ma przypisanych przychodów");
        when(fakeAccountServiceClient.getAccountData("miecio")).thenReturn(AccountDTO.builder().build());
        //when
        calculationService.makeCalculations(CalculationInput.builder().calculationType(CalculationType.INCOME).build(), "miecio");
    }

    @Test
    public void shouldNOTThrowExceptionWithNoIncomesMessage() {
        //given
        LocaleContextHolder.setLocale(Locale.forLanguageTag("pl"));
        expectedException.expect(CalculationException.class);
        expectedException.expectMessage("To konto nie ma przypisanych przychodów");
        when(fakeAccountServiceClient.getAccountData("miecio")).thenReturn(AccountDTO.builder().build());
        //when
        calculationService.makeCalculations(CalculationInput.builder().calculationType(CalculationType.INCOME).build(), "miecio");
    }

    @Test
    public void shouldNOTThrowExceptionWithNoOutcomesAndIncomesMessage() {
        //given
        expectedException.expect(CalculationException.class);
        expectedException.expectMessage("There are no attached incomes and outcomes for current account");
        when(fakeAccountServiceClient.getAccountData("miecio")).thenReturn(AccountDTO.builder().build());
        //when
        calculationService.makeCalculations(CalculationInput.builder().calculationType(CalculationType.BOTH).build(), "miecio");
    }
}
