package com.debski.calculationservice.services;

import com.debski.calculationservice.models.CalculationInput;
import com.debski.calculationservice.models.CalculationOutput;

public interface CalculationService {

    CalculationOutput makeCalculations(CalculationInput input, String username);
}
