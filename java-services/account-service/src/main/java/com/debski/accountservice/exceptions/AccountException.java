package com.debski.accountservice.exceptions;


public class AccountException extends RuntimeException {

    private String message;

    public AccountException(String message) {
        super(message);
    }

}
