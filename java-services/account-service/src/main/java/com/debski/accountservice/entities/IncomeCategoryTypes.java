package com.debski.accountservice.entities;

import lombok.Getter;

@Getter
public enum IncomeCategoryTypes {

    PAYMENT(1L, "payment"),
    GIFT(2L, "gift"),
    BENEFIT(3L, "benefit"),
    OTHER(4L , "other");

    IncomeCategoryTypes(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    private Long id;
    private String name;
}
