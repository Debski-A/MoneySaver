package com.debski.accountservice.entities;

import com.debski.accountservice.entities.enums.Currency;
import com.debski.accountservice.entities.enums.Frequency;
import com.debski.accountservice.entities.enums.OutcomeCategory;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "outcomes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Outcome extends BaseEntity {

    @NotNull
    @Enumerated
    private Frequency frequency;

    @NotNull
    private BigDecimal amount;

    @NotNull
    @Enumerated
    private Currency currency;

    @NotNull
    @Column(name = "date_of_outcome")
    private LocalDate dateOfOutcome;

    @NotNull
    @Column(name = "outcome_category")
    @Enumerated
    private OutcomeCategory outcomeCategory;

    private String note;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_account")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Account account;
}
