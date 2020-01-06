package com.debski.accountservice.models;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class DropdownValuesDTO {

    private List<String> incomeCategories;
    private List<String> outcomeCategories;
    private List<String> currencies;
    private List<String> frequencies;
}
