package com.debski.accountservice.entities;

import com.debski.accountservice.entities.enums.Currency;
import com.debski.accountservice.entities.enums.Frequency;
import com.debski.accountservice.entities.enums.IncomeCategory;
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
    @Column(name = "income_category")
    @Enumerated
    private IncomeCategory incomeCategory;

    private String note;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_account")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Account account;

}
