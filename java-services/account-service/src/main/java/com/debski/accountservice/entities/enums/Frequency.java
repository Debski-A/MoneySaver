package com.debski.accountservice.entities.enums;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum Frequency {
    ONCE, DAILY, MONTHLY, QUARTERLY, YEARLY;
}
