package com.debski.accountservice.services;

import com.debski.accountservice.entities.Account;
import com.debski.accountservice.entities.Income;
import com.debski.accountservice.entities.Outcome;
import com.debski.accountservice.entities.enums.Currency;
import com.debski.accountservice.entities.enums.Frequency;
import com.debski.accountservice.entities.enums.IncomeCategory;
import com.debski.accountservice.entities.enums.OutcomeCategory;
import com.debski.accountservice.exceptions.AccountException;
import com.debski.accountservice.models.AccountDTO;
import com.debski.accountservice.models.DropdownValuesDTO;
import com.debski.accountservice.repositories.AccountRepository;
import com.debski.accountservice.utils.AccountUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@Transactional
public class AccountServiceImpl implements AccountService {

    private AccountRepository repository;

    private AccountUtils accountUtils;

    private ResourceBundleMessageSource messageSource;

    public AccountServiceImpl(AccountRepository repository, AccountUtils accountUtils, ResourceBundleMessageSource messageSource) {
        this.repository = repository;
        this.accountUtils = accountUtils;
        this.messageSource = messageSource;
    }

    @Override
    public AccountDTO findByUsername(String username) {
        Account accountEntity = repository.findByUsername(username);
        AccountDTO accountDto = accountUtils.entityToDto(accountEntity);
        log.debug("Account retrieved from DB by username = {}: {}", username, accountDto);
        return accountDto;
    }

    @Override
    public DropdownValuesDTO provideValuesForDropdowns() {
        DropdownValuesDTO result = DropdownValuesDTO.builder()
                .currencies(translateAllTypes(Currency.EUR))
                .frequencies(translateAllTypes(Frequency.DAILY))
                .incomeCategories(translateAllTypes(IncomeCategory.BENEFIT))
                .outcomeCategories(translateAllTypes(OutcomeCategory.ALCOHOL))
                .build();
        return result;
    }

    private <E extends Enum> List<String> translateAllTypes(E enumSource) {
        List<String> mappedAndTranslatedEnums = Arrays
                .asList(enumSource.getDeclaringClass().getEnumConstants())
                .stream()
                .map(e -> messageSource.getMessage(e.toString().toLowerCase(), null, LocaleContextHolder.getLocale()))
                .collect(Collectors.toList());
        return mappedAndTranslatedEnums;
    }

    @Override
    public AccountDTO update(AccountDTO accountDto) {
        // TODO for now update only for incomes and outcomes
        Account accountEntity = repository.findByUsername(accountDto.getUsername());
        // TODO for now update supports single income/outcome update
        updateIncome(accountEntity, accountDto);
        updateOutcome(accountEntity, accountDto);
        AccountDTO accountDtoResult = accountUtils.entityToDto(accountEntity);
        return accountDtoResult;
    }

    private void updateIncome(Account accountEntity, AccountDTO accountDto) {
        if (accountDto.getIncomes() != null && accountDto.getIncomes().iterator().hasNext()) {
            Income income = accountDto.getIncomes().iterator().next();
            income.setAccount(accountEntity);
            accountEntity.getIncomes().add(income);
        }
    }

    private void updateOutcome(Account accountEntity, AccountDTO accountDto) {
        if (accountDto.getOutcomes() != null && accountDto.getOutcomes().iterator().hasNext()) {
            Outcome outcome = accountDto.getOutcomes().iterator().next();
            outcome.setAccount(accountEntity);
            accountEntity.getOutcomes().add(outcome);
        }
    }

    @Override
    public AccountDTO save(AccountDTO accountDto) {
        validateMandatoryParams(accountDto);
        validatePasswordStrength(accountDto);
        validateIsUsernameAlreadyTaken(accountDto.getUsername());
        Account accountEntity = accountUtils.dtoToEntity(accountDto);
        Account savedAccountEntity = validateEmailAndSave(accountEntity);
        AccountDTO accountDtoResult = accountUtils.entityToDto(savedAccountEntity);

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
        if (repository.existsByUsername(username))
            throw new AccountException(messageSource.getMessage("taken.username", null, LocaleContextHolder.getLocale()));

    }

    private Account validateEmailAndSave(Account accountEntity) throws AccountException {
        Account savedAccountEntity;
        try {
            savedAccountEntity = repository.save(accountEntity);
        } catch (ConstraintViolationException ex) {
            final String[] propertyWhichCauseException = {null};
            ex.getConstraintViolations().forEach(e -> propertyWhichCauseException[0] = e.getPropertyPath().toString());
            throw new AccountException(messageSource.getMessage("invalid." + propertyWhichCauseException[0], null, LocaleContextHolder.getLocale()));
        }
        return savedAccountEntity;
    }

}
