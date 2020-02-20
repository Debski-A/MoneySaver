package com.debski.accountservice.models;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class BudgetDTO implements Comparable<BudgetDTO> {
    private String frequencyDescription;
    private String categoryDescription;
    private String currencyDescription;
    private String budgetTypeDescription;
    private String note;
    private BigDecimal amount;
    private UUID uuid;
    private LocalDate date;

    @Override
    public int compareTo(BudgetDTO o) {
        return this.date.compareTo(o.date);
    }
}
