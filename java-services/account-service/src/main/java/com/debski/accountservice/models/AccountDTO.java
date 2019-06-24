package com.debski.accountservice.models;

import lombok.*;

@Data
@Builder
public class AccountDTO {

    private String username;

    private String rawPassword;

    private String email;
}
