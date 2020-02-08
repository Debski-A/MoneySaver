package com.debski.calculationservice.exceptions;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CalculationErrorDTO {

    private String errorMessage;

    private String source = "account-service";

    public CalculationErrorDTO(String errorMessage) { this.errorMessage = errorMessage;}
}
