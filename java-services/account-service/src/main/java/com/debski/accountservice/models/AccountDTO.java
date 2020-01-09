package com.debski.accountservice.models;

import com.debski.accountservice.entities.Income;
import com.debski.accountservice.entities.Outcome;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {

    private String username;

    @JsonProperty("password")
    private String rawPassword;

    private String email;

    Zrobic IncomeDTO i OutcomeDTO, kore zamiast pola account beda mialy polle String owner z username
    private Set<Income> incomes;

    private Set<Outcome> outcomes;
}
