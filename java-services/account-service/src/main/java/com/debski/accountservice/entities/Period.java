package com.debski.accountservice.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Period {
    ONCE("O"), DAILY("D"), MONTHLY("M"), QUARTERLY("Q"), YEARLY("Y");

    @Getter
    private String mark;
}
