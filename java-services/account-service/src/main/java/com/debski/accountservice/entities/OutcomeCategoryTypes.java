package com.debski.accountservice.entities;

import lombok.Getter;

@Getter
public enum OutcomeCategoryTypes {

    FEE(1L, "fee"),
    FOOD(2L, "food"),
    ALCOHOL(3L, "alcohol"),
    GIFT(4L , "gift"),
    CAR(5L, "car"),
    OTHER(6L,"other");

    OutcomeCategoryTypes(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    private Long id;
    private String name;
}
