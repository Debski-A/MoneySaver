package com.debski.accountservice.entities;

import javax.persistence.Table;

@Table(name = "roles")
public enum Role {
    USER,
    PREMIUN
}
