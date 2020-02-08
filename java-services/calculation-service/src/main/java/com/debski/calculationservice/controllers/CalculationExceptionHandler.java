package com.debski.calculationservice.controllers;

import com.debski.calculationservice.exceptions.CalculationErrorDTO;
import com.debski.calculationservice.exceptions.CalculationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class CalculationExceptionHandler {

    @ExceptionHandler(value = {CalculationException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CalculationErrorDTO handleException(CalculationException ex) {
        log.error(ex.getMessage(), ex);
        return new CalculationErrorDTO(ex.getMessage());
    }
}
