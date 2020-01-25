package com.debski.calculationservice.clients;

import com.debski.calculationservice.enums.Currency;
import com.debski.calculationservice.models.ExchangeRatesContainer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ExchangeRatesClientFallback implements ExchangeRatesClient {

    @Override
    public ExchangeRatesContainer getRates(Currency base) {
        log.error("Couldn't connect with api.exchangeratesapi.io");
        return null;
    }
}
