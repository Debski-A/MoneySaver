package com.debski.accountservice.models;

import com.debski.accountservice.entities.Account;
import com.debski.accountservice.entities.enums.Currency;
import com.debski.accountservice.entities.enums.Frequency;
import com.debski.accountservice.entities.enums.IncomeCategory;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IncomeDTO {

    private Frequency frequency;

    private String frequencyDescription;

    private BigDecimal amount;

    private Currency currency;

    private LocalDate dateOfIncome;

    private IncomeCategory incomeCategory;

    private String incomeCategoryDescription;

    private String note;

    @JsonIgnore
    private Account account;

    private String owner;

    private UUID uuid;
}
