package com.debski.authservice.controllers;

import com.debski.authservice.exceptions.AuthorizationErrorDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class AuthorizationExceptionHandler {

    @ExceptionHandler(value = {OAuth2Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<AuthorizationErrorDTO> handleException(OAuth2Exception ex) {
        log.error(ex.getMessage(), ex);
        AuthorizationErrorDTO errorDTO = new AuthorizationErrorDTO(ex.getMessage());
        HttpStatus httpStatus = HttpStatus.valueOf(ex.getHttpErrorCode());
        return new ResponseEntity<AuthorizationErrorDTO>(errorDTO, httpStatus);
    }
}