package com.debski.accountservice.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {

    private String username;

    @JsonProperty("password")
    private String rawPassword;

    private String email;
}
