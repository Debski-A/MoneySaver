package com.debski.accountservice.services;

import com.debski.accountservice.entities.Account;
import com.debski.accountservice.models.AccountDTO;
import com.debski.accountservice.repositories.RoleRepositoryImpl;
import com.debski.accountservice.entities.RoleTypes;
import org.apache.commons.lang.CharUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.function.IntPredicate;

@Component
class AccountUtils {

    private RoleRepositoryImpl roleRepository;
    private PasswordEncoder encoder;

    AccountUtils(RoleRepositoryImpl roleRepository, PasswordEncoder encoder) {
        this.roleRepository = roleRepository;
        this.encoder = encoder;
    }

    AccountDTO entityToDto(Account entity) {
        AccountDTO dto = entity != null ? AccountDTO.builder()
                .username(entity.getUsername())
                .email(entity.getEmail())
                .build() : null;
        return dto;
    }

    Account dtoToEntity(AccountDTO dto) {

        Account entity = Account.builder().username(dto.getUsername()).password(encoder.encode(dto.getRawPassword())).email(dto.getEmail()).roles(Collections.singleton(roleRepository.getRole(RoleTypes.USER))).enabled(true).build();
        return entity;
    }

    boolean isToWeak(String password) {
        IntPredicate isUpperCase =  (singleChar -> CharUtils.isAsciiAlphaUpper((char) singleChar));
        IntPredicate isDigit = (singleChar -> CharUtils.isAsciiNumeric((char) singleChar));

        if (password.length() < 8 || password.chars().noneMatch(isUpperCase) || password.chars().noneMatch(isDigit)) return true;
        return false;
    }

}
