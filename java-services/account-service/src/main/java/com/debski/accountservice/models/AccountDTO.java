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

    private Set<Income> incomes;

    private Set<Outcome> outcomes;
}
