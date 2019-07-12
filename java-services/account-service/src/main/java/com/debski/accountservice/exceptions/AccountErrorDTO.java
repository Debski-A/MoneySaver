package com.debski.accountservice.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountErrorDTO {
    private String errorMessage;

    public AccountErrorDTO(String errorMessage) { this.errorMessage = errorMessage;}
}
