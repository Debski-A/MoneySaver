package com.debski.calculationservice.models;

import com.debski.calculationservice.enums.Currency;
import com.debski.calculationservice.enums.Frequency;
import com.debski.calculationservice.enums.OutcomeCategory;
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

//    @JsonIgnore
//    private Account account;

    private String owner;
}
