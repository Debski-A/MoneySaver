package com.debski.accountservice.entities;

import lombok.Getter;

@Getter
public enum RoleTypes {
    USER(1L, "ROLE_USER"),
    PREMIUM(2L, "ROLE_PREMIUM");

    RoleTypes(Long id, String roleName) {
        this.id = id;
        this.roleName = roleName;
    }

    private Long id;
    private String roleName;

}
