package com.debski.accountservice.models;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class BudgetDropdownValuesDTO {

    private List<String> incomeCategories;
    private List<String> outcomeCategories;
    private List<String> currencies;
    private List<String> periods;
}
