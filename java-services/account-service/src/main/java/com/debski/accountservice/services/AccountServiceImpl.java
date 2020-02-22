package com.debski.accountservice.services;

import com.debski.accountservice.entities.Account;
import com.debski.accountservice.entities.Income;
import com.debski.accountservice.entities.Outcome;
import com.debski.accountservice.exceptions.AccountException;
import com.debski.accountservice.models.AccountDTO;
import com.debski.accountservice.models.IncomeDTO;
import com.debski.accountservice.models.OutcomeDTO;
import com.debski.accountservice.repositories.AccountRepository;
import com.debski.accountservice.utils.AccountUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;
import java.util.*;
import java.util.stream.Stream;

@Service
@Slf4j
@Transactional
public class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepository;

    private AccountUtils accountUtils;

    private MessageSource messageSource;

    public AccountServiceImpl(AccountRepository accountRepository, AccountUtils accountUtils, MessageSource messageSource) {
        this.accountRepository = accountRepository;
        this.accountUtils = accountUtils;
        this.messageSource = messageSource;
    }

    @Override
    public AccountDTO findByUsername(String username) {
        Account accountEntity = accountRepository.findByUsername(username);
        AccountDTO accountDto = accountUtils.accountEntityToDto(accountEntity);
        log.debug("Account retrieved from DB by username = {}: {}", username, accountDto);
        return accountDto;
    }

    @Override
    public AccountDTO update(AccountDTO accountDto) {
        // TODO for now update only for incomes and outcomes
        Account accountEntity = accountRepository.findByUsername(accountDto.getUsername());
        if (accountEntity == null) {
            throw new AccountException(messageSource.getMessage("user.not.found", null, LocaleContextHolder.getLocale()));
        }
        // TODO for now update supports single income/outcome update
        updateIncome(accountEntity, accountDto);
        updateOutcome(accountEntity, accountDto);
        validateConstraintsAndSave(accountEntity);
        AccountDTO accountDtoResult = accountUtils.accountEntityToDto(accountEntity);
        return accountDtoResult;
    }

    private void updateIncome(Account accountEntity, AccountDTO accountDto) {
        if (accountDto.getIncomes() != null && accountDto.getIncomes().iterator().hasNext()) {
            IncomeDTO incomeDTO = accountDto.getIncomes().iterator().next();
            Income income =accountUtils.incomeDtoToEntity(incomeDTO);
            income.setAccount(accountEntity);
            if (accountEntity.getIncomes() == null) {
                accountEntity.setIncomes(new HashSet<>() {{ add(income); }});
            } else {
                accountEntity.getIncomes().add(income);
            }
        }
    }

    private void updateOutcome(Account accountEntity, AccountDTO accountDto) {
        if (accountDto.getOutcomes() != null && accountDto.getOutcomes().iterator().hasNext()) {
            OutcomeDTO outcomeDTO = accountDto.getOutcomes().iterator().next();
            Outcome outcome = accountUtils.outcomeDtoToEntity(outcomeDTO);
            outcome.setAccount(accountEntity);
            if (accountEntity.getOutcomes() == null) {
                accountEntity.setOutcomes(new HashSet<>() {{ add(outcome); }});
            } else {
                accountEntity.getOutcomes().add(outcome);
            }
        }
    }

    @Override
    public AccountDTO save(AccountDTO accountDto) {
        validateMandatoryParams(accountDto);
        validatePasswordStrength(accountDto);
        validateIsUsernameAlreadyTaken(accountDto.getUsername());
        Account accountEntity = accountUtils.accountDtoToEntity(accountDto);
        Account savedAccountEntity = validateConstraintsAndSave(accountEntity);
        AccountDTO accountDtoResult = accountUtils.accountEntityToDto(savedAccountEntity);

        log.debug("Account with username: {} was saved", accountDtoResult.getUsername());
        return accountDtoResult;
    }

    private void validateMandatoryParams(AccountDTO accountDto) throws AccountException {
        // Mandatory Params: username, password. email
        Stream.of(accountDto.getUsername(), accountDto.getRawPassword(), accountDto.getEmail()).forEach(field -> {
            if (StringUtils.isBlank(field)) {
                throw new AccountException(messageSource.getMessage("mandatory.parameters", null, LocaleContextHolder.getLocale()));
            }
        });
    }

    private void validatePasswordStrength(AccountDTO accountDto) {
        if (accountUtils.isToWeak(accountDto.getRawPassword()))
            throw new AccountException(messageSource.getMessage("weak.password", null, LocaleContextHolder.getLocale()));
    }

    private void validateIsUsernameAlreadyTaken(String username) throws AccountException {
        if (accountRepository.existsByUsername(username))
            throw new AccountException(messageSource.getMessage("taken.username", null, LocaleContextHolder.getLocale()));

    }

    private Account validateConstraintsAndSave(Account accountEntity) throws AccountException {
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
