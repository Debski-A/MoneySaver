package com.debski.accountservice.models;

import com.debski.accountservice.entities.enums.BudgetType;
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
    private String note;
    private String currency;
    private BigDecimal amount;
    private UUID uuid;
    private LocalDate date;
    private BudgetType budgetType;

    @Override
    public int compareTo(BudgetDTO o) {
        return this.date.compareTo(o.date);
    }
}
