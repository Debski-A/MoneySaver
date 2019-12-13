package com.debski.accountservice.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Currency {

    PLN("PLN", "zł"),
    EUR("EUR", "€"),
    USD("USD", "$"),
    GBP("GBP", "£");

    private String name;
    private String symbol;
}
