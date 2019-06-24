package com.debski.accountservice.services;

import com.debski.accountservice.entities.Account;
import com.debski.accountservice.entities.Role;
import com.debski.accountservice.models.AccountDTO;
import com.debski.accountservice.repositories.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@Slf4j
@Transactional
public class AccountServiceImpl implements AccountService{

    private AccountRepository repository;

    private PasswordEncoder encoder;

    public AccountServiceImpl(AccountRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    public void save(AccountDTO accountDto) {
        Account accountEntity = Account.builder()
                .username(accountDto.getUsername())
                .password(encoder.encode(accountDto.getRawPassword()))
                .email(accountDto.getEmail())
                .roles(Collections.singleton(Role.USER))
                .build();

        repository.save(accountEntity);
        log.debug("Account for {} was saved", accountEntity.getUsername());
    }

    public AccountDTO findByUsername(String username) {
        Account accountEntity = repository.findByUsername(username);
        AccountDTO accountDto = accountEntity != null ? AccountDTO.builder()
                .username(accountEntity.getUsername())
                .email(accountEntity.getEmail())
                .build() : null;
        log.debug("Account retrieved from DB by username = {}: {}", username, accountDto);
        return accountDto;
    }

    public boolean existsByUsername(String username) {
        return repository.existsByUsername(username);
    }
}
