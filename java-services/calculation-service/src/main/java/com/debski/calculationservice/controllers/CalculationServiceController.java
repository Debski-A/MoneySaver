package com.debski.calculationservice.controllers;

import com.debski.calculationservice.models.CalculationInput;
import com.debski.calculationservice.models.CalculationOutput;
import com.debski.calculationservice.services.CalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class CalculationServiceController {

    @Autowired
    private CalculationService calculationService;

    @PreAuthorize("#oauth2.hasScope('ui')")
    @PostMapping("/calculate")
    public CalculationOutput makeCalculations(Principal principal,CalculationInput input) {
        return calculationService.makeCalculations(input, principal.getName());
    }
}
