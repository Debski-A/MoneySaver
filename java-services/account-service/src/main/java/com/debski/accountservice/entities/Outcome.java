package com.debski.accountservice.entities;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "outcomes")
@Data
@Builder
public class Outcome extends BaseEntity {

    private Period frequency;

    private BigDecimal amount;

    private LocalDate dateOfOutcome;

    @OneToOne(fetch = FetchType.EAGER, cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    @JoinColumn(name = "id")
    private OutcomeCategory outcomeCategory;

    private String note;

    @ManyToOne
    @JoinColumn(name = "id_account")
    private Account account;
}
