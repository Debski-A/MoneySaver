package com.debski.accountservice.entities;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum Frequency {
    ONCE, DAILY, MONTHLY, QUARTERLY, YEARLY;
}
