package com.debski.authservice.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class AccountDbUserDetailsService implements UserDetailsService {

    //TODO ta klasa chyba do usuniecia tak samo jak User i UserPrincipal - sprawdz to
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
