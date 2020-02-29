package com.debski.calculationservice.clients;

import com.debski.calculationservice.enums.Currency;
import com.debski.calculationservice.models.ExchangeRatesContainer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

@Component
@Slf4j
public class ExchangeRatesClientFallback implements ExchangeRatesClient {

    @Override
    public ExchangeRatesContainer getRates(Currency base) {
        log.warn("Couldn't connect with api.exchangeratesapi.io. Using hardcoded rates as a fallback");
        ExchangeRatesContainer rates = new ExchangeRatesContainer();
        switch (base) {
            // Actual rates at day 5.02.2020
            case PLN: {
                rates.setBase(base);
                rates.setRates(Map.of("PLN", BigDecimal.valueOf(1),
                        "USD", BigDecimal.valueOf(0.2594196418),
                        "GBP", BigDecimal.valueOf(0.1987338495),
                        "EUR", BigDecimal.valueOf(0.2353439552)));
                break;
            }
            case EUR: {
                rates.setBase(base);
                rates.setRates(Map.of("EUR", BigDecimal.valueOf(1),
                        "USD", BigDecimal.valueOf(1.1023),
                        "GBP", BigDecimal.valueOf(0.84444),
                        "PLN", BigDecimal.valueOf(4.2491)));
                break;
            }
            case GBP: {
                rates.setBase(base);
                rates.setRates(Map.of("GBP", BigDecimal.valueOf(1),
                        "USD", BigDecimal.valueOf(1.3053621335),
                        "EUR", BigDecimal.valueOf(1.184216759),
                        "PLN", BigDecimal.valueOf(5.0318554308)));
                break;
            }
            case USD: {
                rates.setBase(base);
                rates.setRates(Map.of("USD", BigDecimal.valueOf(1),
                        "GBP", BigDecimal.valueOf(0.7660709426),
                        "EUR", BigDecimal.valueOf(0.9071940488),
                        "PLN", BigDecimal.valueOf(3.8547582328)));
                break;
            }
        }
        log.debug("Base of rates container: {}. available rates: {}", rates.getBase(), rates.getRates());
        return rates;
    }
}
