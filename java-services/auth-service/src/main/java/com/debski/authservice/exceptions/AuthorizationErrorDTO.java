package com.debski.authservice.exceptions;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AuthorizationErrorDTO {

    private String errorMessage;

    private String source = "auth-service";

    public AuthorizationErrorDTO(String errorMessage) { this.errorMessage = errorMessage;}
}
