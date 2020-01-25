package com.debski.calculationservice.services;

import com.debski.calculationservice.clients.AccountServiceClient;
import com.debski.calculationservice.clients.ExchangeRatesClient;
import com.debski.calculationservice.enums.CalculationType;
import com.debski.calculationservice.enums.Currency;
import com.debski.calculationservice.enums.IncomeCategory;
import com.debski.calculationservice.models.CalculationInput;
import com.debski.calculationservice.models.CalculationOutput;
import com.debski.calculationservice.models.ExchangeRatesContainer;
import com.debski.calculationservice.clients.FakeAccountServiceClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class CalculationServiceUnitTest {

    @Spy
    private AccountServiceClient fakeAccountServiceClient = new FakeAccountServiceClient();

    @Mock
    private ExchangeRatesClient fakeExchangeRatesClient;

    @InjectMocks
    private CalculationService calculationService = new CalculationServiceImpl(fakeAccountServiceClient, fakeExchangeRatesClient);


    @Test
    public void test() {
        //given
        ExchangeRatesContainer container = new ExchangeRatesContainer();
        container.setBase(Currency.PLN);
        container.setRates(Map.of(Currency.USD.name(), BigDecimal.valueOf(0.2613398054)));
        Mockito.when(fakeExchangeRatesClient.getRates(Currency.PLN)).thenReturn(container);

        CalculationInput input = CalculationInput.builder()
                .calculationType(CalculationType.INCOME)
                .incomeCategory(IncomeCategory.PAYMENT)
                .currency(Currency.PLN)
                .startDate(LocalDate.of(2019, 1, 1))
                .endDate(LocalDate.of(2020, 1, 1))
                .build();
        //when
        CalculationOutput calculationOutput = calculationService.makeCalculations(input, "miecio");
        //then
//        assertThat(calculationOutput.getCalculationType(), equalTo(CalculationType.INCOME));
//        assertThat(calculationOutput.getCurrency(), equalTo(Currency.PLN));
//        assertThat(calculationOutput.getIncomeCategory(), equalTo(IncomeCategory.PAYMENT));

    }
}
