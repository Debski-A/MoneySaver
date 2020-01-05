package com.debski.accountservice.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "outcome_categories")
@Getter
@Setter
@ToString
public class OutcomeCategory {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @NotEmpty
    private String name;
}
