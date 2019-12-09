package com.debski.accountservice.exceptions;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AccountException extends RuntimeException {

    private String message;


    public AccountException(String message) {
        super(message);
        this.message = message;
    }

}
