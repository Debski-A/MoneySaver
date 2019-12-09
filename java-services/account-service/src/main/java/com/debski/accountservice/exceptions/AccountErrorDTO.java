package com.debski.accountservice.exceptions;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AccountErrorDTO {

    private String errorMessage;

    private String source = "account-service";

    public AccountErrorDTO(String errorMessage) { this.errorMessage = errorMessage;}
}
