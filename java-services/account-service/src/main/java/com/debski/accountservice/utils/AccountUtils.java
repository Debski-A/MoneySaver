package com.debski.accountservice.utils;

import com.debski.accountservice.entities.Account;
import com.debski.accountservice.entities.Income;
import com.debski.accountservice.entities.Outcome;
import com.debski.accountservice.entities.enums.Role;
import com.debski.accountservice.models.AccountDTO;
import com.debski.accountservice.models.IncomeDTO;
import com.debski.accountservice.models.OutcomeDTO;
import org.apache.commons.lang.CharUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.function.IntPredicate;
import java.util.stream.Collectors;

@Component
public class AccountUtils {

    private PasswordEncoder encoder;

    public AccountUtils(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    public AccountDTO accountEntityToDto(Account entity) {
        Set<IncomeDTO> incomesDTO = null;
        Set<OutcomeDTO> outcomesDTO = null;
        AccountDTO dto = entity != null ? AccountDTO.builder()
                .username(entity.getUsername())
                .email(entity.getEmail())
                .build() : null;
        //TODO cos tego za duzo
        if (entity != null) {
            if(entity.getIncomes() != null) {
                incomesDTO = entity.getIncomes().stream().map(income -> incomeEntityToDto(income)).collect(Collectors.toSet());
            }
            dto.setIncomes(incomesDTO);

            if(entity.getOutcomes() != null) {
                outcomesDTO = entity.getOutcomes().stream().map(outcome -> outcomeEntityToDto(outcome)).collect(Collectors.toSet());
            }
            dto.setOutcomes(outcomesDTO);
        }
        return dto;
    }

    public Account accountDtoToEntity(AccountDTO dto) {
        Set<Income> incomes = null;
        Set<Outcome> outcomes = null;
        Account entity = Account.builder()
                .username(dto.getUsername())
                .password(encoder.encode(dto.getRawPassword()))
                .email(dto.getEmail())
                .role(Role.USER) // TODO Role.PREMIUM will be available by account upgrade
                .enabled(true)
                .build();
        if (dto.getIncomes() != null) {
            incomes = dto.getIncomes().stream().map(incomeDTO -> {
                Income income = incomeDtoToEntity(incomeDTO);
                income.setAccount(entity);
                return income;
            }).collect(Collectors.toSet());
        }
        if (dto.getOutcomes() != null) {
            outcomes = dto.getOutcomes().stream().map(outcomeDTO -> {
                Outcome outcome = outcomeDtoToEntity(outcomeDTO);
                outcome.setAccount(entity);
                return outcome;
            }).collect(Collectors.toSet());
        }
        entity.setIncomes(incomes);
        entity.setOutcomes(outcomes);
        return entity;
    }

    public Income incomeDtoToEntity(IncomeDTO dto) {
        return Income.builder()
                .amount(dto.getAmount())
                .frequency(dto.getFrequency())
                .dateOfIncome(dto.getDateOfIncome())
                .incomeCategory(dto.getIncomeCategory())
                .currency(dto.getCurrency())
                .note(dto.getNote())
                .build();
    }

    public IncomeDTO incomeEntityToDto(Income entity) {
        return IncomeDTO.builder()
                .amount(entity.getAmount())
                .frequency(entity.getFrequency())
                .dateOfIncome(entity.getDateOfIncome())
                .incomeCategory(entity.getIncomeCategory())
                .currency(entity.getCurrency())
                .note(entity.getNote())
                .owner(entity.getAccount() == null ? null : entity.getAccount().getUsername())
                .build();
    }

    public Outcome outcomeDtoToEntity(OutcomeDTO dto) {
        return Outcome.builder()
                .amount(dto.getAmount())
                .frequency(dto.getFrequency())
                .dateOfOutcome(dto.getDateOfOutcome())
                .outcomeCategory(dto.getOutcomeCategory())
                .currency(dto.getCurrency())
                .note(dto.getNote())
                .build();
    }

    public OutcomeDTO outcomeEntityToDto(Outcome entity) {
        return OutcomeDTO.builder()
                .amount(entity.getAmount())
                .frequency(entity.getFrequency())
                .dateOfOutcome(entity.getDateOfOutcome())
                .outcomeCategory(entity.getOutcomeCategory())
                .currency(entity.getCurrency())
                .note(entity.getNote())
                .owner(entity.getAccount() == null ? null : entity.getAccount().getUsername())
                .build();
    }

    public boolean isToWeak(String password) {
        IntPredicate isUpperCase = (singleChar -> CharUtils.isAsciiAlphaUpper((char) singleChar));
        IntPredicate isDigit = (singleChar -> CharUtils.isAsciiNumeric((char) singleChar));

        if (password.length() < 8 || password.chars().noneMatch(isUpperCase) || password.chars().noneMatch(isDigit))
            return true;
        return false;
    }

}
