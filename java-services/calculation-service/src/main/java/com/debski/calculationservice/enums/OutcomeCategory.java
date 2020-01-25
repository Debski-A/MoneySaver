package com.debski.calculationservice.enums;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
//@JsonIgnoreProperties(ignoreUnknown = true) byc moze to bedzie potrzebne z uwagi na NONE
public enum OutcomeCategory {
    FEE, FOOD, ALCOHOL, GIFT, CAR, OTHER, NONE
}
