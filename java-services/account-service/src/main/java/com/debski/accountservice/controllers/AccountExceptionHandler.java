package com.debski.accountservice.controllers;

import com.debski.accountservice.exceptions.AccountErrorDTO;
import com.debski.accountservice.exceptions.AccountException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
@CrossOrigin
public class AccountExceptionHandler {

    @ExceptionHandler(value = {AccountException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public AccountErrorDTO handleException(AccountException ex) {
        log.error(ex.getMessage(), ex);
        return new AccountErrorDTO(ex.getMessage());
    }
}
