package com.debski.calculationservice.clients;

import com.debski.calculationservice.enums.Currency;
import com.debski.calculationservice.enums.Frequency;
import com.debski.calculationservice.enums.IncomeCategory;
import com.debski.calculationservice.enums.OutcomeCategory;
import com.debski.calculationservice.models.AccountDTO;
import com.debski.calculationservice.models.IncomeDTO;
import com.debski.calculationservice.models.OutcomeDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class FakeAccountServiceClient implements AccountServiceClient {

    @Override
    public AccountDTO getAccountData(String username) {
        AccountDTO account1 = AccountDTO.builder()
                .incomes(prepareIncomes())
                .outcomes(prepareOutcomes())
                .username("miecio")
                .email("miecio@wp.pl")
                .rawPassword("Password1")
                .build();
        return account1;
    }

    private Set<IncomeDTO> prepareIncomes() {
        IncomeDTO income1 = IncomeDTO.builder()
                .amount(BigDecimal.valueOf(3000))
                .currency(Currency.PLN)
                .dateOfIncome(LocalDate.of(2019, 1, 28))
                .frequency(Frequency.MONTHLY)
                .incomeCategory(IncomeCategory.PAYMENT)
                .note("Wypłata")
                .owner("miecio")
                .build();
        IncomeDTO income2 = IncomeDTO.builder()
                .amount(BigDecimal.valueOf(100))
                .currency(Currency.GBP)
                .dateOfIncome(LocalDate.of(2019, 4, 21))
                .frequency(Frequency.ONCE)
                .incomeCategory(IncomeCategory.GIFT)
                .note("Na urodziny od cioci z Anglii")
                .owner("miecio")
                .build();
        IncomeDTO income3 = IncomeDTO.builder()
                .amount(BigDecimal.valueOf(500))
                .currency(Currency.PLN)
                .dateOfIncome(LocalDate.of(2018, 10, 2))
                .frequency(Frequency.QUARTERLY)
                .incomeCategory(IncomeCategory.OTHER)
                .note("Procent składany")
                .owner("miecio")
                .build();
        IncomeDTO income4 = IncomeDTO.builder()
                .amount(BigDecimal.valueOf(200))
                .currency(Currency.USD)
                .dateOfIncome(LocalDate.of(2019, 11, 11))
                .frequency(Frequency.ONCE)
                .incomeCategory(IncomeCategory.PAYMENT)
                .note("zlecenie z USA")
                .owner("miecio")
                .build();
        return new HashSet<>() {{add(income1); add(income2); add(income3); add(income4);}};
    }

    private Set<OutcomeDTO> prepareOutcomes() {
        OutcomeDTO outcome1 = OutcomeDTO.builder()
                .amount(BigDecimal.valueOf(200))
                .currency(Currency.PLN)
                .dateOfOutcome(LocalDate.of(2019, 1, 24))
                .frequency(Frequency.ONCE)
                .outcomeCategory(OutcomeCategory.FOOD)
                .note("grubsze zakupy")
                .owner("miecio")
                .build();
        OutcomeDTO outcome2 = OutcomeDTO.builder()
                .amount(BigDecimal.valueOf(700))
                .currency(Currency.PLN)
                .dateOfOutcome(LocalDate.of(2019, 1, 31))
                .frequency(Frequency.MONTHLY)
                .outcomeCategory(OutcomeCategory.FEE)
                .note("komorne")
                .owner("miecio")
                .build();
        OutcomeDTO outcome3 = OutcomeDTO.builder()
                .amount(BigDecimal.valueOf(250))
                .currency(Currency.EUR)
                .dateOfOutcome(LocalDate.of(2019, 3, 21))
                .frequency(Frequency.QUARTERLY)
                .outcomeCategory(OutcomeCategory.OTHER)
                .note("wpłata oszczędności na lokate")
                .owner("miecio")
                .build();
        OutcomeDTO outcome4 = OutcomeDTO.builder()
                .amount(BigDecimal.valueOf(20))
                .currency(Currency.PLN)
                .dateOfOutcome(LocalDate.of(2019, 2, 2))
                .frequency(Frequency.DAILY)
                .outcomeCategory(OutcomeCategory.FOOD)
                .note("codzienny obiad w pracy")
                .owner("miecio")
                .build();
        return new HashSet<>() {{add(outcome1); add(outcome2); add(outcome3); add(outcome4);}};
    }
}
