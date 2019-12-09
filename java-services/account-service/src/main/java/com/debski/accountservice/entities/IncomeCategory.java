package com.debski.accountservice.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "income_categories")
@Getter
@Setter
public class IncomeCategory extends BaseEntity {

    private String name;

}
