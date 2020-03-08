package com.debski.accountservice.models;

import com.debski.accountservice.enums.FinanceType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class FinanceDescriptionDTO implements Comparable<FinanceDescriptionDTO> {
    private String frequencyDescription;
    private String categoryDescription;
    private String currencyDescription;
    private String note;
    private String financeTypeDescription;
    private FinanceType financeType;
    private BigDecimal amount;
    private UUID uuid;
    private LocalDate date;

    @Override
    public int compareTo(FinanceDescriptionDTO o) {
        return this.date.compareTo(o.date);
    }
}
