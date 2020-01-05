package com.debski.accountservice.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class BudgetCategoriesServiceIntegrationTest {

    @Autowired
    private BudgetCategoriesServiceImpl budgetCategoriesService;

    @Test
    public void shouldReturnTranslatedAllIncomeCategories() {
        //given
        LocaleContextHolder.setLocale(Locale.forLanguageTag("en"));
        List<String> expectedResult = List.of("Payment", "Gift", "Benefit", "Other");
        //when
        List<String> incomeCategories = budgetCategoriesService.getIncomeCategories();
        //then
        assertThat(incomeCategories, equalTo(expectedResult) );
    }

    @Test
    public void shouldReturnTranslatedAllOutcomeCategories() {
        //given
        LocaleContextHolder.setLocale(Locale.forLanguageTag("pl"));
        List<String> expectedResult = List.of("Opłata", "Jedzenie", "Alkohol", "Prezent", "Samochód", "Inne");
        //when
        List<String> outcomeCategories = budgetCategoriesService.getOutcomeCategories();
        //then
        assertThat(outcomeCategories, equalTo(expectedResult) );
    }
}
