package com.debski.accountservice.services;

import com.debski.accountservice.entities.Account;
import com.debski.accountservice.entities.Income;
import com.debski.accountservice.entities.Outcome;
import com.debski.accountservice.exceptions.AccountException;
import com.debski.accountservice.models.AccountDTO;
import com.debski.accountservice.models.IncomeDTO;
import com.debski.accountservice.models.OutcomeDTO;
import com.debski.accountservice.repositories.AccountRepository;
import com.debski.accountservice.utils.TransformationUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;
import java.util.HashSet;
import java.util.function.IntPredicate;

@Service
@Slf4j
@Transactional
public class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepository;

    private TransformationUtils transformationUtils;

    private MessageSource messageSource;

    private PasswordEncoder encoder;

    public AccountServiceImpl(AccountRepository accountRepository, TransformationUtils transformationUtils, MessageSource messageSource, PasswordEncoder encoder) {
        this.accountRepository = accountRepository;
        this.transformationUtils = transformationUtils;
        this.messageSource = messageSource;
        this.encoder = encoder;
    }

    @Override
    public AccountDTO findByUsername(String username) {
        Account accountEntity = accountRepository.findByUsername(username);
        AccountDTO accountDto = transformationUtils.entityToDto(accountEntity);
        log.debug("Account retrieved from DB by username = {}: {}", username, accountDto);
        return accountDto;
    }

    @Override
    public void save(AccountDTO accountDto) {
        validateMandatoryParams(accountDto);
        validatePasswordStrength(accountDto);
        validateIsUsernameAlreadyTaken(accountDto.getUsername());
        Account accountEntity = transformationUtils.dtoToEntity(accountDto);
        accountEntity.setPassword(encoder.encode(accountEntity.getPassword()));
        validateEntityConstraintsAndSave(accountEntity);
        log.debug("Account with username: {} was saved", accountDto.getUsername());
    }

    private void validateMandatoryParams(AccountDTO accountDto) throws AccountException {
        if (StringUtils.isAnyBlank(accountDto.getUsername(), accountDto.getRawPassword(), accountDto.getEmail())) {
            throw new AccountException(messageSource.getMessage("mandatory.parameters", null, LocaleContextHolder.getLocale()));
        }
    }

    private void validatePasswordStrength(AccountDTO accountDto) {
        if (isToWeak(accountDto.getRawPassword()))
            throw new AccountException(messageSource.getMessage("weak.password", null, LocaleContextHolder.getLocale()));
    }

    public boolean isToWeak(String password) {
        IntPredicate isUpperCase = (singleChar -> CharUtils.isAsciiAlphaUpper((char) singleChar));
        IntPredicate isDigit = (singleChar -> CharUtils.isAsciiNumeric((char) singleChar));

        if (password.length() < 8 || password.chars().noneMatch(isUpperCase) || password.chars().noneMatch(isDigit))
            return true;
        return false;
    }

    private void validateIsUsernameAlreadyTaken(String username) throws AccountException {
        if (accountRepository.existsByUsername(username))
            throw new AccountException(messageSource.getMessage("taken.username", null, LocaleContextHolder.getLocale()));

    }

    @Override
    public void addIncome(IncomeDTO income) {
        Account accountEntity = accountRepository.findByUsername(income.getOwner());
        if (accountEntity == null) {
            throw new AccountException(messageSource.getMessage("user.not.found", null, LocaleContextHolder.getLocale()));
        }
        Income incomeEntity = transformationUtils.incomeDtoToEntity(income);
        incomeEntity.setAccount(accountEntity);
        if (accountEntity.getIncomes() == null) {
            accountEntity.setIncomes(new HashSet<>() {{ add(incomeEntity); }});
        } else {
            accountEntity.getIncomes().add(incomeEntity);
        }
        validateEntityConstraintsAndSave(accountEntity);
    }

    @Override
    public void addOutcome(OutcomeDTO outcome) {
        Account accountEntity = accountRepository.findByUsername(outcome.getOwner());
        if (accountEntity == null) {
            throw new AccountException(messageSource.getMessage("user.not.found", null, LocaleContextHolder.getLocale()));
        }
        Outcome outcomeEntity = transformationUtils.outcomeDtoToEntity(outcome);
        outcomeEntity.setAccount(accountEntity);
        if (accountEntity.getOutcomes() == null) {
            accountEntity.setOutcomes(new HashSet<>() {{ add(outcomeEntity); }});
        } else {
            accountEntity.getOutcomes().add(outcomeEntity);
        }
        validateEntityConstraintsAndSave(accountEntity);
    }


    private Account validateEntityConstraintsAndSave(Account accountEntity) throws AccountException {
        Account savedAccountEntity;
        try {
            savedAccountEntity = accountRepository.save(accountEntity);
        } catch (ConstraintViolationException ex) {
            final String[] propertyWhichCauseException = {null};
            ex.getConstraintViolations().forEach(e -> propertyWhichCauseException[0] = e.getPropertyPath().toString());
            throw new AccountException(messageSource.getMessage("invalid." + propertyWhichCauseException[0], null, LocaleContextHolder.getLocale()));
        }
        return savedAccountEntity;
    }

}
