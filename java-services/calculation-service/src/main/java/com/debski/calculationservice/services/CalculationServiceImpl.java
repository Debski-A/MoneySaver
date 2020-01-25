package com.debski.calculationservice.services;

import com.debski.calculationservice.clients.AccountServiceClient;
import com.debski.calculationservice.clients.ExchangeRatesClient;
import com.debski.calculationservice.enums.Currency;
import com.debski.calculationservice.models.AccountDTO;
import com.debski.calculationservice.models.CalculationInput;
import com.debski.calculationservice.models.CalculationOutput;
import org.springframework.stereotype.Service;

@Service
public class CalculationServiceImpl implements CalculationService {

    private AccountServiceClient accountServiceClient;

    private ExchangeRatesClient exchangeRatesClient;

    public CalculationServiceImpl(AccountServiceClient accountServiceClient, ExchangeRatesClient exchangeRatesClient) {
        this.accountServiceClient = accountServiceClient;
        this.exchangeRatesClient = exchangeRatesClient;
    }

    @Override
    public CalculationOutput makeCalculations(CalculationInput input, String username) {
        //TODO test gdy accService zwroci , albo error ze username not exist
        AccountDTO accountData = accountServiceClient.getAccountData(username);
        exchangeRatesClient.getRates(Currency.PLN);
        //TODO wyciagnac incomes i outcomes, ktore pasuja do zasiegu podanym w input
        // pamietac ze jest Frequency i te ktore nie sa ONCE powinny sie kumulowac
        // zrobic 3 drogi? dla samego incomes, dla samego outcomes i dla obu na raz
        return null;
    }
}
