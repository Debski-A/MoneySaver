package com.debski.accountservice.entities;

public enum RoleTypes {
    USER(1L, "ROLE_USER"),
    PREMIUM(2L, "ROLE_PREMIUM");

    RoleTypes(Long id, String type) {
        this.id = id;
        this.type = type;
    }

    String type;
    Long id;

}
