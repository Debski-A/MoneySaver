package com.debski.calculationservice.models;

import com.debski.calculationservice.enums.Currency;
import com.debski.calculationservice.enums.Frequency;
import com.debski.calculationservice.enums.OutcomeCategory;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class OutcomeDTO {

    private Frequency frequency;

    private BigDecimal amount;

    private Currency currency;

    private LocalDate dateOfOutcome;

    private OutcomeCategory outcomeCategory;

    private String note;

    private String owner;

    private UUID uuid;

    public static OutcomeDTO clone(OutcomeDTO original) {
        return OutcomeDTO.builder()
                .frequency(original.getFrequency())
                .amount(original.getAmount())
                .currency(original.getCurrency())
                .dateOfOutcome(original.getDateOfOutcome())
                .outcomeCategory(original.getOutcomeCategory())
                .note(original.getNote())
                .owner(original.getOwner())
                .uuid(UUID.randomUUID())
                .build();
    }
}
