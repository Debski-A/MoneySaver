package com.debski.calculationservice.models;

import com.debski.calculationservice.enums.CalculationType;
import com.debski.calculationservice.enums.Currency;
import com.debski.calculationservice.enums.IncomeCategory;
import com.debski.calculationservice.enums.OutcomeCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.TreeSet;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CalculationOutput {

    private TreeSet<VisualisationPoint> visualisationPoints;

    private CalculationType calculationType;

    private OutcomeCategory outcomeCategory;

    private IncomeCategory incomeCategory;

    private Currency currency;
}
