package com.debski.authservice.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@CrossOrigin
public class AuthorizationController {

    @RequestMapping(value = "/accounts/current", method = RequestMethod.GET)
    public Principal getUser(Principal principal) {
        return principal;
    }
}
