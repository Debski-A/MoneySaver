package com.debski.accountservice.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "outcome_categories")
@Getter
@Setter
public class OutcomeCategory {

    private String name;
}
