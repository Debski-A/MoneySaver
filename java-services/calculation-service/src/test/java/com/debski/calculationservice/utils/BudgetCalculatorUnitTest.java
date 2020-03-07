package com.debski.calculationservice.utils;

import com.debski.calculationservice.clients.ExchangeRatesClient;
import com.debski.calculationservice.enums.*;
import com.debski.calculationservice.enums.Currency;
import com.debski.calculationservice.exceptions.CalculationException;
import com.debski.calculationservice.models.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.MessageSource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(MockitoJUnitRunner.class)
public class BudgetCalculatorUnitTest {

    @Mock
    private ExchangeRatesClient fakeExchangeRatesClient;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private BudgetCalculator budgetCalculator;

    @Test
    public void shouldReturnIncomesWithinSpecifiedDateTimePeriod() {
        //should return one income
        //given
        Set<IncomeDTO> incomes = Set.of(IncomeDTO.builder().dateOfIncome(LocalDate.of(2019, 2, 2)).build());
        //when
        Set<IncomeDTO> filteredIncomes = budgetCalculator.filterIncomesByDateTimePeriod(incomes, LocalDate.of(2019, 2, 1), LocalDate.of(2019, 2, 3));
        //then
        assertThat(filteredIncomes, hasSize(1));
    }

    @Test
    public void shouldReturnIncomesWithinSpecifiedDateTimePeriod_2() {
        //should NOT return any income
        //given
        Set<IncomeDTO> incomes = Set.of(IncomeDTO.builder().dateOfIncome(LocalDate.of(2019, 2, 4)).build());
        //when
        Set<IncomeDTO> filteredIncomes = budgetCalculator.filterIncomesByDateTimePeriod(incomes, LocalDate.of(2019, 2, 1), LocalDate.of(2019, 2, 3));
        //then
        assertThat(filteredIncomes, hasSize(0));
    }

    @Test
    public void shouldReturnOutcomesAfterSuccessfulExchange() {
        //given
        ExchangeRatesContainer container = new ExchangeRatesContainer();
        container.setBase(Currency.PLN);
        container.setRates(Map.of(Currency.USD.name(), BigDecimal.valueOf(0.3)));
        Mockito.when(fakeExchangeRatesClient.getRates(Currency.PLN)).thenReturn(container);
        OutcomeDTO outcome1 = OutcomeDTO.builder()
                .amount(BigDecimal.valueOf(300))
                .currency(Currency.PLN)
                .build();
        OutcomeDTO outcome2 = OutcomeDTO.builder()
                .amount(BigDecimal.valueOf(1200))
                .currency(Currency.PLN)
                .build();
        //when
        budgetCalculator.adjustOutcomesCurrencies(Set.of(outcome1, outcome2), Currency.USD);
        //then
        assertThat(outcome1.getAmount(), equalTo(BigDecimal.valueOf(90.00).setScale(2)));
        assertThat(outcome1.getCurrency(), equalTo(Currency.USD));
        assertThat(outcome2.getAmount(), equalTo(BigDecimal.valueOf(360.00).setScale(2)));
        assertThat(outcome2.getCurrency(), equalTo(Currency.USD));
    }

    @Test(expected = CalculationException.class)
    public void shouldThrowCalculationExceptionIfCurrencyUnsupported() {
        //given
        ExchangeRatesContainer container = new ExchangeRatesContainer();
        container.setBase(Currency.PLN);
        container.setRates(Map.of(Currency.USD.name(), BigDecimal.valueOf(0.3)));
        Mockito.when(fakeExchangeRatesClient.getRates(Currency.PLN)).thenReturn(container);
        OutcomeDTO outcome1 = OutcomeDTO.builder()
                .amount(BigDecimal.valueOf(300))
                .currency(Currency.PLN)
                .build();
        //when
        budgetCalculator.adjustOutcomesCurrencies(Set.of(outcome1), Currency.GBP); //Mock only serves rates for USD and PLN (which is base)
        //then expect Exception
    }

    @Test
    public void shouldRoundAmountAfterMultiplication() {
        //given
        ExchangeRatesContainer container = new ExchangeRatesContainer();
        container.setBase(Currency.EUR);
        container.setRates(Map.of(Currency.PLN.name(), BigDecimal.valueOf(4.2753)));
        Mockito.when(fakeExchangeRatesClient.getRates(Currency.EUR)).thenReturn(container);
        IncomeDTO income1 = IncomeDTO.builder()
                .amount(BigDecimal.valueOf(444))
                .currency(Currency.EUR)
                .build();
        //when
        budgetCalculator.adjustIncomesCurrencies(Set.of(income1), Currency.PLN);
        //then
        assertThat(income1.getCurrency(), equalTo(Currency.PLN));
        assertThat(income1.getAmount(),equalTo(BigDecimal.valueOf(1898.23)));
    }

    @Test
    public void shouldConvertIncomesAccordingToFrequencies() {
        //given
        IncomeDTO dailyIncome = IncomeDTO.builder()
                .frequency(Frequency.DAILY)
                .dateOfIncome(LocalDate.of(2019, 1, 1))
                .amount(BigDecimal.valueOf(2))
                .currency(Currency.PLN)
                .incomeCategory(IncomeCategory.OTHER)
                .note("xyz")
                .owner("miecio")
                .build();
        IncomeDTO monthlyIncome = IncomeDTO.builder()
                .frequency(Frequency.MONTHLY)
                .dateOfIncome(LocalDate.of(2019, 1, 15))
                .amount(BigDecimal.valueOf(15))
                .currency(Currency.PLN)
                .owner("miecio")
                .incomeCategory(IncomeCategory.PAYMENT)
                .note("rachunek")
                .build();
        Set<IncomeDTO> incomes = new HashSet<>() {{
            add(dailyIncome);
            add(monthlyIncome);
        }};
        LocalDate startDate = LocalDate.of(2019, 1, 1);
        LocalDate endDate = LocalDate.of(2019, 3, 15);
        //when
        Set<IncomeDTO> convertedIncomes = budgetCalculator.convertIncomesFrequencies(incomes, endDate);
        //then
        assertThat(convertedIncomes, hasSize(77));
    }

    @Test
    public void shouldConvertOutcomesAccordingToFrequencies() {
        //given
        OutcomeDTO outcome1 = OutcomeDTO.builder()
                .amount(BigDecimal.valueOf(1))
                .dateOfOutcome(LocalDate.of(2020, 2, 25))
                .outcomeCategory(OutcomeCategory.GIFT)
                .frequency(Frequency.DAILY)
                .currency(Currency.PLN)
                .note("pajacyk.pl")
                .owner("miecio")
                .build();
        OutcomeDTO outcome2 = OutcomeDTO.builder()
                .amount(BigDecimal.valueOf(100))
                .dateOfOutcome(LocalDate.of(2020, 1, 29))
                .outcomeCategory(OutcomeCategory.FEE)
                .frequency(Frequency.MONTHLY)
                .currency(Currency.GBP)
                .note("dla Mariuszka na życie w Anglii")
                .owner("miecio")
                .build();

        Set<OutcomeDTO> outcomes = new HashSet<>() {{
            add(outcome1);
            add(outcome2);
        }};
        //when
        Set<OutcomeDTO> convertedOutcomes = budgetCalculator.convertOutcomesFrequencies(outcomes, LocalDate.of(2020, 2, 29));
        //then
        assertThat(convertedOutcomes, hasSize(7));
    }

    @Test
    public void identicalTwoIncomesShouldNotBeOverrideInSet() {
        //given
        IncomeDTO income1 = IncomeDTO.builder()
                .amount(BigDecimal.valueOf(20))
                .dateOfIncome(LocalDate.of(2020, 2, 20))
                .currency(Currency.EUR)
                .frequency(Frequency.ONCE)
                .note("xyz")
                .incomeCategory(IncomeCategory.OTHER)
                .owner("miecio")
                .build();
        IncomeDTO income2 = IncomeDTO.builder()
                .amount(BigDecimal.valueOf(20))
                .dateOfIncome(LocalDate.of(2020, 2, 20))
                .currency(Currency.EUR)
                .frequency(Frequency.ONCE)
                .note("xyz")
                .incomeCategory(IncomeCategory.OTHER)
                .owner("miecio")
                .build();
        //when
        Set<IncomeDTO> incomes = new HashSet<>() {{
            add(income1); //wont be override, because equals and hashcode are not override, so implementation for equals and hashcode comes from Object.class
            add(income2); //thus hashcode is based on memory allocation, so those two elements are not treated as duplicates (because each was instantiated by 'new')
        }};
        Set<IncomeDTO> convertedIncomes = budgetCalculator.convertIncomesFrequencies(incomes, LocalDate.of(2020,2,20));
        //then
        assertThat(convertedIncomes, hasSize(2));
    }

    @Test
    public void identicalTwoOutcomesShouldNotBeOverrideInSet() {
        //given
        OutcomeDTO outcome1 = OutcomeDTO.builder()
                .amount(BigDecimal.valueOf(33))
                .frequency(Frequency.ONCE)
                .currency(Currency.EUR)
                .owner("olek")
                .note("hmmm")
                .dateOfOutcome(LocalDate.of(2019, 3, 4))
                .outcomeCategory(OutcomeCategory.ALCOHOL)
                .build();
        OutcomeDTO outcome2 = OutcomeDTO.builder()
                .amount(BigDecimal.valueOf(33))
                .frequency(Frequency.ONCE)
                .currency(Currency.EUR)
                .owner("olek")
                .note("hmmm")
                .dateOfOutcome(LocalDate.of(2019, 3, 4))
                .outcomeCategory(OutcomeCategory.ALCOHOL)
                .build();
        //when
        Set<OutcomeDTO> outcomes = new HashSet<>() {{ //mind that explanation below works only for HASH Set
            add(outcome1); //wont be override, because equals and hashcode are not override, so implementation for equals and hashcode comes from Object.class
            add(outcome2); //thus hashcode is based on memory allocation, so those two elements are not treated as duplicates (because each was instantiated by 'new')
        }};
        Set<OutcomeDTO> convertedOutcomes = budgetCalculator.convertOutcomesFrequencies(outcomes, LocalDate.of(2020,2,20));
        //then
        assertThat(convertedOutcomes, hasSize(2));
    }

    @Test
    public void shouldMergeIncomesAndOutcomesAndReturnCalculationOutput() {
        //given
        IncomeDTO i1 = IncomeDTO.builder()
                .amount(BigDecimal.valueOf(100))
                .currency(Currency.PLN)
                .dateOfIncome(LocalDate.of(2019, 2, 2))
                .build();
        IncomeDTO i2 = IncomeDTO.builder()
                .amount(BigDecimal.valueOf(200))
                .currency(Currency.PLN)
                .dateOfIncome(LocalDate.of(2019, 3, 3))
                .build();
        IncomeDTO i3 = IncomeDTO.builder()
                .amount(BigDecimal.valueOf(300))
                .currency(Currency.PLN)
                .dateOfIncome(LocalDate.of(2019, 4, 4))
                .build();

        OutcomeDTO o1 = OutcomeDTO.builder()
                .amount(BigDecimal.valueOf(50))
                .currency(Currency.PLN)
                .dateOfOutcome(LocalDate.of(2019, 2, 4))
                .build();
        OutcomeDTO o2 = OutcomeDTO.builder()
                .amount(BigDecimal.valueOf(300))
                .currency(Currency.PLN)
                .dateOfOutcome(LocalDate.of(2019, 3, 3))
                .build();
        OutcomeDTO o3 = OutcomeDTO.builder()
                .amount(BigDecimal.valueOf(400))
                .currency(Currency.PLN)
                .dateOfOutcome(LocalDate.of(2019,4,3))
                .build();
        // This should give -150 for last item in CalculationOutput.visualisationPoints
        //when
        CalculationOutput calculationOutput = budgetCalculator.mergeIncomesAndOutcomes(Set.of(i1, i2, i3), Set.of(o1, o2, o3), Currency.PLN);
        //then
        assertThat(calculationOutput.getCalculationType(), equalTo(CalculationType.BOTH));
        assertThat(calculationOutput.getOutcomeCategory(), nullValue());
        assertThat(calculationOutput.getIncomeCategory(), nullValue());
        assertThat(calculationOutput.getCurrency(), equalTo(Currency.PLN));
        assertThat(calculationOutput.getVisualisationPoints(), hasSize(5));

        Iterator<VisualisationPoint> vpsIterator = calculationOutput.getVisualisationPoints().iterator();
        VisualisationPoint vp1 = vpsIterator.next();
        assertThat(vp1.getValue(), equalTo(BigDecimal.valueOf(100)));
        assertThat(vp1.getDate(), equalTo(LocalDate.of(2019,2,2)));

        VisualisationPoint vp2 = vpsIterator.next();
        assertThat(vp2.getValue(), equalTo(BigDecimal.valueOf(50)));
        assertThat(vp2.getDate(), equalTo(LocalDate.of(2019,2,4)));

        VisualisationPoint vp3 = vpsIterator.next();
        assertThat(vp3.getValue(), equalTo(BigDecimal.valueOf(-50)));
        assertThat(vp3.getDate(), equalTo(LocalDate.of(2019,3,3)));

        VisualisationPoint vp4 = vpsIterator.next();
        assertThat(vp4.getValue(), equalTo(BigDecimal.valueOf(-450)));
        assertThat(vp4.getDate(), equalTo(LocalDate.of(2019,4,3)));

        VisualisationPoint vp5 = vpsIterator.next();
        assertThat(vp5.getValue(), equalTo(BigDecimal.valueOf(-150)));
        assertThat(vp5.getDate(), equalTo(LocalDate.of(2019,4,4)));
    }

    @Test
    public void shouldMergeIncomesAndOutcomesAndReturnCalculationOutput2() {
        //given
        IncomeDTO i1 = IncomeDTO.builder()
                .amount(BigDecimal.valueOf(100))
                .currency(Currency.PLN)
                .dateOfIncome(LocalDate.of(2019, 2, 2))
                .build();
        IncomeDTO i2 = IncomeDTO.builder()
                .amount(BigDecimal.valueOf(200))
                .currency(Currency.PLN)
                .dateOfIncome(LocalDate.of(2019, 2, 2))
                .build();
        IncomeDTO i3 = IncomeDTO.builder()
                .amount(BigDecimal.valueOf(300))
                .currency(Currency.PLN)
                .dateOfIncome(LocalDate.of(2019, 3, 3))
                .build();
        IncomeDTO i4 = IncomeDTO.builder()
                .amount(BigDecimal.valueOf(400))
                .currency(Currency.PLN)
                .dateOfIncome(LocalDate.of(2019, 3, 3))
                .build();
        IncomeDTO i5 = IncomeDTO.builder()
                .amount(BigDecimal.valueOf(500))
                .currency(Currency.PLN)
                .dateOfIncome(LocalDate.of(2019, 4, 4))
                .build();

        OutcomeDTO o1 = OutcomeDTO.builder()
                .amount(BigDecimal.valueOf(10))
                .currency(Currency.PLN)
                .dateOfOutcome(LocalDate.of(2019, 3, 3))
                .build();
        OutcomeDTO o2 = OutcomeDTO.builder()
                .amount(BigDecimal.valueOf(20))
                .currency(Currency.PLN)
                .dateOfOutcome(LocalDate.of(2019, 3, 3))
                .build();
        OutcomeDTO o3 = OutcomeDTO.builder()
                .amount(BigDecimal.valueOf(30))
                .currency(Currency.PLN)
                .dateOfOutcome(LocalDate.of(2019,4,4))
                .build();
        OutcomeDTO o4 = OutcomeDTO.builder()
                .amount(BigDecimal.valueOf(4000))
                .currency(Currency.PLN)
                .dateOfOutcome(LocalDate.of(2019,4,4))
                .build();
        // This should give -150 for last item in CalculationOutput.visualisationPoints
        //when
        CalculationOutput calculationOutput = budgetCalculator.mergeIncomesAndOutcomes(Set.of(i1, i2, i3, i4, i5), Set.of(o1, o2, o3, o4), Currency.PLN);
        //then
        Iterator<VisualisationPoint> vpsIterator = calculationOutput.getVisualisationPoints().iterator();
        assertThat(calculationOutput.getCalculationType(), equalTo(CalculationType.BOTH));
        assertThat(calculationOutput.getOutcomeCategory(), nullValue());
        assertThat(calculationOutput.getIncomeCategory(), nullValue());
        assertThat(calculationOutput.getCurrency(), equalTo(Currency.PLN));
        assertThat(calculationOutput.getVisualisationPoints(), hasSize(3));
        assertThat(vpsIterator.next(), comparesEqualTo(new VisualisationPoint(BigDecimal.valueOf(300), LocalDate.of(2019,2,2))));
        assertThat(vpsIterator.next(), comparesEqualTo(new VisualisationPoint(BigDecimal.valueOf(970), LocalDate.of(2019,3,3))));
        assertThat(vpsIterator.next(), comparesEqualTo(new VisualisationPoint(BigDecimal.valueOf(-2560), LocalDate.of(2019,4,4))));
    }

    @Test
    public void shouldReturn01_03_2020() {
        //given
        LocalDate someDate = LocalDate.of(2020,03, 14);
        //when
        LocalDate result = budgetCalculator.specifyBeginningOfMonth(someDate);
        //then
        assertThat(result, equalTo(LocalDate.of(2020, 03,01)));
    }

    @Test
    public void shouldReturn28_02_2019() {
        //given
        LocalDate someDate = LocalDate.of(2019,02, 14);
        //when
        LocalDate result = budgetCalculator.specifyEndOfMonth(someDate);
        //then
        assertThat(result, equalTo(LocalDate.of(2019, 02,28)));
    }

    @Test
    public void shouldReturn29_02_2020() {
        //given
        LocalDate someDate = LocalDate.of(2020,02, 14);
        //when
        LocalDate result = budgetCalculator.specifyEndOfMonth(someDate);
        //then
        assertThat(result, equalTo(LocalDate.of(2020, 02,29)));
    }

    @Test
    public void shouldReturnFilteredPaymentIncomes() {
        //given
        IncomeDTO i1 = IncomeDTO.builder()
                .incomeCategory(IncomeCategory.PAYMENT)
                .build();
        IncomeDTO i2 = IncomeDTO.builder()
                .incomeCategory(IncomeCategory.OTHER)
                .build();
        IncomeDTO i3 = IncomeDTO.builder()
                .incomeCategory(IncomeCategory.PAYMENT)
                .build();
        IncomeDTO i4 = IncomeDTO.builder()
                .incomeCategory(IncomeCategory.GIFT)
                .build();
        //when
        Set<IncomeDTO> result = budgetCalculator.filterIncomesByCategory(Set.of(i1, i2, i3, i4), IncomeCategory.PAYMENT);
        //then
        assertThat(result, hasSize(2));
    }

    @Test
    public void shouldCalculateIncomesOutput() {
        //given
        IncomeDTO i1 = IncomeDTO.builder()
                .amount(BigDecimal.valueOf(100))
                .dateOfIncome(LocalDate.of(2019, 2, 2))
                .build();
        IncomeDTO i2 = IncomeDTO.builder()
                .amount(BigDecimal.valueOf(200))
                .dateOfIncome(LocalDate.of(2019, 2, 2))
                .build();
        IncomeDTO i3 = IncomeDTO.builder()
                .amount(BigDecimal.valueOf(300))
                .dateOfIncome(LocalDate.of(2019, 3, 3))
                .build();
        IncomeDTO i4 = IncomeDTO.builder()
                .amount(BigDecimal.valueOf(400))
                .dateOfIncome(LocalDate.of(2019, 3, 3))
                .build();
        IncomeDTO i5 = IncomeDTO.builder()
                .amount(BigDecimal.valueOf(500))
                .dateOfIncome(LocalDate.of(2019, 4, 4))
                .build();
        //when
        Set<VisualisationPoint> visualisationPoints = budgetCalculator.calculateVisualizationPoints(Set.of(i1, i2, i3, i4, i5));
        //then
        Iterator<VisualisationPoint> vpsIterator = visualisationPoints.iterator();
        assertThat(vpsIterator.next(), comparesEqualTo(new VisualisationPoint(BigDecimal.valueOf(300), LocalDate.of(2019,2,2))));
        assertThat(vpsIterator.next(), comparesEqualTo(new VisualisationPoint(BigDecimal.valueOf(700), LocalDate.of(2019,3,3))));
        assertThat(vpsIterator.next(), comparesEqualTo(new VisualisationPoint(BigDecimal.valueOf(500), LocalDate.of(2019,4,4))));
    }

    @Test
    public void shouldFill0AmountValueVisualizationPointsForEmptyDaysOfMonths() {
        //given
        Set<VisualisationPoint> vps = new TreeSet<>();
        vps.add(new VisualisationPoint(BigDecimal.valueOf(400), LocalDate.of(2020,3,13)));
        vps.add(new VisualisationPoint(BigDecimal.valueOf(100), LocalDate.of(2020, 3, 24)));
        LocalDate beginningOfMonth = LocalDate.of(2020, 3,1);
        LocalDate endOfMonth = LocalDate.of(2020, 3,31);
        //when
        Set<VisualisationPoint> result = budgetCalculator.fillRestOfDaysOfMonthWithZeroAmountValue(vps, beginningOfMonth, endOfMonth);
        //then
        assertThat(result, hasSize(31));
    }


}
