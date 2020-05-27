package com.debski.calculationservice.services;

import com.debski.calculationservice.clients.AccountServiceClient;
import com.debski.calculationservice.clients.ExchangeRatesClient;
import com.debski.calculationservice.clients.FakeAccountServiceClient;
import com.debski.calculationservice.enums.CalculationType;
import com.debski.calculationservice.enums.Currency;
import com.debski.calculationservice.enums.Frequency;
import com.debski.calculationservice.enums.IncomeCategory;
import com.debski.calculationservice.exceptions.CalculationException;
import com.debski.calculationservice.models.*;
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
import java.util.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
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
        containerGBP.setRates(Map.of(Currency.PLN.name(), BigDecimal.valueOf(5)));
        when(fakeExchangeRatesClient.getRates(Currency.GBP)).thenReturn(containerGBP);

        ExchangeRatesContainer containerEUR = new ExchangeRatesContainer();
        containerEUR.setBase(Currency.EUR);
        containerEUR.setRates(Map.of(Currency.PLN.name(), BigDecimal.valueOf(4.5)));
        when(fakeExchangeRatesClient.getRates(Currency.EUR)).thenReturn(containerEUR);

        ExchangeRatesContainer containerUSD = new ExchangeRatesContainer();
        containerUSD.setBase(Currency.USD);
        containerUSD.setRates(Map.of(Currency.PLN.name(), BigDecimal.valueOf(4)));
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
//        assertThat(calculationOutput.getVisualisationPoints(), hasSize(31));
        //after corrections : ###
        assertThat(calculationOutput.getVisualisationPoints(), hasSize(35));
        assertAllVisualisationPoints(calculationOutput.getVisualisationPoints());
        // ######################
        assertThat(calculationOutput.getCalculationType(), equalTo(CalculationType.BOTH));
        assertThat(calculationOutput.getCurrency(), equalTo(Currency.PLN));
        assertThat(calculationOutput.getIncomeCategory(), nullValue());
        assertThat(calculationOutput.getOutcomeCategory(), nullValue());
    }

    private void assertAllVisualisationPoints(Set<VisualisationPoint> visualisationPoints) {
        Iterator<VisualisationPoint> iter = visualisationPoints.iterator();
        assertThat(iter.next().getValue(), equalTo(new BigDecimal("500.00"))); //extra 1
        assertThat(iter.next().getValue(), equalTo(new BigDecimal("0.00")));
        assertThat(iter.next().getValue(), equalTo(new BigDecimal("-700.00")));
        assertThat(iter.next().getValue(), equalTo(new BigDecimal("2300.00")));
        assertThat(iter.next().getValue(), equalTo(new BigDecimal("1600.00")));
        assertThat(iter.next().getValue(), equalTo(new BigDecimal("4600.00")));
        assertThat(iter.next().getValue(), equalTo(new BigDecimal("3475.00")));
        assertThat(iter.next().getValue(), equalTo(new BigDecimal("2775.00")));
        assertThat(iter.next().getValue(), equalTo(new BigDecimal("5775.00")));
        assertThat(iter.next().getValue(), equalTo(new BigDecimal("6275.00"))); //extra 2
        assertThat(iter.next().getValue(), equalTo(new BigDecimal("6775.00")));
        assertThat(iter.next().getValue(), equalTo(new BigDecimal("6075.00")));
        assertThat(iter.next().getValue(), equalTo(new BigDecimal("9075.00")));
        assertThat(iter.next().getValue(), equalTo(new BigDecimal("8375.00")));
        assertThat(iter.next().getValue(), equalTo(new BigDecimal("11375.00")));
        assertThat(iter.next().getValue(), equalTo(new BigDecimal("10250.00")));
        assertThat(iter.next().getValue(), equalTo(new BigDecimal("9550.00")));
        assertThat(iter.next().getValue(), equalTo(new BigDecimal("12550.00")));
        assertThat(iter.next().getValue(), equalTo(new BigDecimal("13050.00"))); //extra 3 miedzy 2019-06-28 a 2019
        assertThat(iter.next().getValue(), equalTo(new BigDecimal("12350.00")));
        assertThat(iter.next().getValue(), equalTo(new BigDecimal("15350.00")));
        assertThat(iter.next().getValue(), equalTo(new BigDecimal("14650.00")));
        assertThat(iter.next().getValue(), equalTo(new BigDecimal("17650.00")));
        assertThat(iter.next().getValue(), equalTo(new BigDecimal("16525.00")));
        assertThat(iter.next().getValue(), equalTo(new BigDecimal("15825.00")));
        assertThat(iter.next().getValue(), equalTo(new BigDecimal("18825.00")));
        assertThat(iter.next().getValue(), equalTo(new BigDecimal("19325.00"))); // extra 4 ostatnie miedzie
        // 2019-09-28 a 2019-10-27
        assertThat(iter.next().getValue(), equalTo(new BigDecimal("18625.00"))); // 2019-10-27
        assertThat(iter.next().getValue(), equalTo(new BigDecimal("21625.00"))); //10-28
        assertThat(iter.next().getValue(), equalTo(new BigDecimal("22425.00"))); //11-11
        assertThat(iter.next().getValue(), equalTo(new BigDecimal("21725.00"))); //11-27
        assertThat(iter.next().getValue(), equalTo(new BigDecimal("24725.00"))); //11-28
        assertThat(iter.next().getValue(), equalTo(new BigDecimal("23600.00"))); //12-21
        assertThat(iter.next().getValue(), equalTo(new BigDecimal("22900.00")));//12-27
        assertThat(iter.next().getValue(), equalTo(new BigDecimal("25900.00")));//12 -28
        assertThat(iter.hasNext(), equalTo(false));
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

    @Test
    public void shouldCalculateIncomes() {
        //given
        ExchangeRatesContainer containerGBP = new ExchangeRatesContainer();
        containerGBP.setBase(Currency.GBP);
        containerGBP.setRates(Map.of(Currency.PLN.name(), BigDecimal.valueOf(5.23343533457)));
        AccountDTO miecio = AccountDTO.builder()
                .incomes(prepareIncomes())
                .username("miecio")
                .build();
        when(fakeAccountServiceClient.getAccountData("miecio")).thenReturn(miecio);
        when(fakeExchangeRatesClient.getRates(Currency.GBP)).thenReturn(containerGBP);
        CalculationInput input = CalculationInput.builder()
                .calculationType(CalculationType.INCOME)
                .currency(Currency.PLN)
                .incomeCategory(IncomeCategory.PAYMENT)
                .startDate(LocalDate.of(2020, 02, 02))
                .build();
        //when
        CalculationOutput result = calculationService.makeCalculations(input, "miecio");
        //them
        assertThat(result.getVisualisationPoints(), hasSize(29));
        Iterator<VisualisationPoint> vpsIterator = result.getVisualisationPoints().iterator();
    }

    @Test
    public void shouldCalculateIncomesAndAdd0ValueVisualizationPointsForEmptyDaysOfMonths() {
        //given
        IncomeDTO i1 = IncomeDTO.builder()
                .amount(BigDecimal.valueOf(200))
                .currency(Currency.PLN)
                .dateOfIncome(LocalDate.of(2020, 3, 4))
                .frequency(Frequency.ONCE)
                .incomeCategory(IncomeCategory.GIFT)
                .note("zlecenie z USA")
                .owner("miecio")
                .build();
        IncomeDTO i2 = IncomeDTO.builder()
                .amount(BigDecimal.valueOf(100))
                .currency(Currency.PLN)
                .dateOfIncome(LocalDate.of(2020, 3, 8))
                .frequency(Frequency.ONCE)
                .incomeCategory(IncomeCategory.GIFT)
                .note("hmmm")
                .owner("miecio")
                .build();
        AccountDTO miecio = AccountDTO.builder()
                .incomes(Set.of(i1,i2))
                .username("miecio")
                .build();
        when(fakeAccountServiceClient.getAccountData("miecio")).thenReturn(miecio);
        CalculationInput input = CalculationInput.builder()
                .calculationType(CalculationType.INCOME)
                .currency(Currency.PLN)
                .incomeCategory(IncomeCategory.GIFT)
                .startDate(LocalDate.of(2020, 03, 31))
                .build();
        //when
        CalculationOutput result = calculationService.makeCalculations(input, "miecio");
        //them
        assertThat(result.getVisualisationPoints(), hasSize(31));
        Iterator<VisualisationPoint> vpsIterator = result.getVisualisationPoints().iterator();
    }

    @Test
    public void shouldThrowExceptionWhenAfterCategoryFilteringIncomesAreEmpty() {
        //given
        LocaleContextHolder.setLocale(Locale.forLanguageTag("pl"));
        IncomeDTO i1 = IncomeDTO.builder()
                .amount(BigDecimal.valueOf(200))
                .currency(Currency.PLN)
                .dateOfIncome(LocalDate.of(2020, 3, 4))
                .frequency(Frequency.ONCE)
                .incomeCategory(IncomeCategory.GIFT)
                .note("zlecenie z USA")
                .owner("miecio")
                .build();
        AccountDTO miecio = AccountDTO.builder()
                .incomes(Set.of(i1))
                .username("miecio")
                .build();
        when(fakeAccountServiceClient.getAccountData("miecio")).thenReturn(miecio);
        CalculationInput input = CalculationInput.builder()
                .calculationType(CalculationType.INCOME)
                .currency(Currency.PLN)
                .incomeCategory(IncomeCategory.PAYMENT)
                .startDate(LocalDate.of(2020, 03, 31))
                .build();
        //when
        expectedException.expect(CalculationException.class);
        expectedException.expectMessage("Nie ma wydatków i przychodów dla podanej kategori");
        calculationService.makeCalculations(input, "miecio");

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

    private Set<IncomeDTO> prepareIncomes() {
        IncomeDTO income1 = IncomeDTO.builder()
                .amount(BigDecimal.valueOf(200))
                .currency(Currency.PLN)
                .dateOfIncome(LocalDate.of(2019, 1, 28))
                .frequency(Frequency.DAILY)
                .incomeCategory(IncomeCategory.PAYMENT)
                .note("Wypłata")
                .owner("miecio")
                .build();
        IncomeDTO income2 = IncomeDTO.builder()
                .amount(BigDecimal.valueOf(100)) //523.34 after exchange
                .currency(Currency.GBP)
                .dateOfIncome(LocalDate.of(2020, 2, 21))
                .frequency(Frequency.ONCE)
                .incomeCategory(IncomeCategory.PAYMENT)
                .note("Na urodziny od cioci z Anglii")
                .owner("miecio")
                .build();
        IncomeDTO income3 = IncomeDTO.builder()
                .amount(BigDecimal.valueOf(500))
                .currency(Currency.PLN)
                .dateOfIncome(LocalDate.of(2019, 3, 30)) //should be added at 29.02.2020
                .frequency(Frequency.MONTHLY)
                .incomeCategory(IncomeCategory.PAYMENT)
                .note("Procent składany")
                .owner("miecio")
                .build();
        IncomeDTO income4 = IncomeDTO.builder()
                .amount(BigDecimal.valueOf(200))
                .currency(Currency.PLN)
                .dateOfIncome(LocalDate.of(2020, 02, 04))
                .frequency(Frequency.ONCE)
                .incomeCategory(IncomeCategory.GIFT) // should be avoided because different category
                .note("zlecenie z USA")
                .owner("miecio")
                .build();
        return new HashSet<>() {{add(income1); add(income2); add(income3); add(income4);}};
    }
}
