package com.debski.accountservice.entities;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.math.BigDecimal;
import java.time.LocalDate;
@Entity
@Table(name = "incomes")
@Data
@Builder
public class Income extends BaseEntity{

    @NotNull
    @Enumerated
    private Frequency frequency;

    @NotNull
    private BigDecimal amount;

    @NotNull
    @Enumerated
    private Currency currency;

    @NotNull
    @Column(name = "date_of_income")
    @PastOrPresent
    private LocalDate dateOfIncome;

    @NotNull
    @OneToOne(fetch = FetchType.EAGER, cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    @JoinColumn(name = "id_category")
    private IncomeCategory incomeCategory;

    private String note;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_account")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Account account;

}
