package com.debski.accountservice.services;

import com.debski.accountservice.entities.*;
import com.debski.accountservice.models.BudgetDropdownValuesDTO;
import com.debski.accountservice.repositories.BudgetRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class BudgetServiceImpl implements BudgetService {

    private static final String CATEGORY_PREFIX="category.";

    private BudgetRepository budgetRepository;

    private ResourceBundleMessageSource messageSource;

    public BudgetServiceImpl(BudgetRepository budgetRepository, ResourceBundleMessageSource messageSource) {
        this.budgetRepository = budgetRepository;
        this.messageSource = messageSource;
    }


    @Override
    public void saveAccountIncome(String username, Income income) {

    }

    @Override
    public void saveAccountOutcome(String username, Outcome outcome) {

    }

    @Override
    public BudgetDropdownValuesDTO getBudgetDropdownValues() {
        return null;
    }

    private List<String> getIncomeCategories() {
        List<IncomeCategory> allIncomeCategories = budgetRepository.getAllIncomeCategories();
        List<String> i18nAllIncomeCategories = allIncomeCategories
                .stream()
                .map(incomeCategory -> messageSource.getMessage(CATEGORY_PREFIX + incomeCategory.getName(), null, LocaleContextHolder.getLocale()))
                .collect(Collectors.toList());
        return i18nAllIncomeCategories;
    }

    private List<String> getOutcomeCategories() {
        List<OutcomeCategory> allOutcomeCategories = budgetRepository.getAllOutcomeCategories();
        log.debug("outcomeCategories from database = {}", allOutcomeCategories);
        List<String> i18nAllOutcomeCategories = allOutcomeCategories
                .stream()
                .map(outcomeCategory -> messageSource.getMessage(CATEGORY_PREFIX + outcomeCategory.getName(), null, LocaleContextHolder.getLocale()))
                .collect(Collectors.toList());
        return i18nAllOutcomeCategories;
    }

}
