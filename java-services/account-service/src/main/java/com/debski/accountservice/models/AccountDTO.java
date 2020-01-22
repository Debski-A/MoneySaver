package com.debski.accountservice.models;

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

    private Set<IncomeDTO> incomes;

    private Set<OutcomeDTO> outcomes;
}
