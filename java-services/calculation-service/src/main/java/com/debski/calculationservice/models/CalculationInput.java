package com.debski.calculationservice.models;

import com.debski.calculationservice.enums.CalculationType;
import com.debski.calculationservice.enums.Currency;
import com.debski.calculationservice.enums.IncomeCategory;
import com.debski.calculationservice.enums.OutcomeCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CalculationInput {

    private CalculationType calculationType;

    private LocalDate startDate;

    private LocalDate endDate;

    private IncomeCategory incomeCategory;

    private OutcomeCategory outcomeCategory;

    private Currency currency;
}
