package com.debski.accountservice.models;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class DropdownValuesDTO {

    private Map<Integer, String> incomeCategories;
    private Map<Integer, String> outcomeCategories;
    private Map<Integer, String> currencies;
    private Map<Integer, String> frequencies;
}
