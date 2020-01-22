package com.debski.accountservice.models;

import com.debski.accountservice.entities.Account;
import com.debski.accountservice.entities.enums.Currency;
import com.debski.accountservice.entities.enums.Frequency;
import com.debski.accountservice.entities.enums.OutcomeCategory;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OutcomeDTO {
    private Frequency frequency;

    private BigDecimal amount;

    private Currency currency;

    private LocalDate dateOfOutcome;

    private OutcomeCategory outcomeCategory;

    private String note;

    @JsonIgnore
    private Account account;

    private String owner;
}
