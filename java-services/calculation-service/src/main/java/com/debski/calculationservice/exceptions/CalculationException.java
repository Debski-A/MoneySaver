package com.debski.calculationservice.exceptions;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CalculationException extends RuntimeException {

    private String message;


    public CalculationException(String message) {
        super(message);
        this.message = message;
    }

}
