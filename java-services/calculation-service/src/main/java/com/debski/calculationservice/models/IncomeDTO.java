package com.debski.calculationservice.models;

import com.debski.calculationservice.enums.Currency;
import com.debski.calculationservice.enums.Frequency;
import com.debski.calculationservice.enums.IncomeCategory;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class IncomeDTO {

    private Frequency frequency;

    private BigDecimal amount;

    private Currency currency;

    private LocalDate dateOfIncome;

    private IncomeCategory incomeCategory;

    private String note;

    private String owner;

    public static IncomeDTO clone(IncomeDTO original) {
        return IncomeDTO.builder()
               .amount(original.getAmount())
               .incomeCategory(original.getIncomeCategory())
                .frequency(original.getFrequency())
                .dateOfIncome(original.getDateOfIncome())
                .currency(original.getCurrency())
                .owner(original.getOwner())
                .note(original.getNote())
                .build();
    }

}
