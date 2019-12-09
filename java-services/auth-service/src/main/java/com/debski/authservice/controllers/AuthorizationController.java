package com.debski.authservice.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class AuthorizationController {

    @RequestMapping(value = "/accounts/current", method = RequestMethod.GET)
    public Principal getUser(Principal principal) {
        return principal;
    }
}
