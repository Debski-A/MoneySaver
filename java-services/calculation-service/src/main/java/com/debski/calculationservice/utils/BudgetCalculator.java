package com.debski.calculationservice.utils;

import com.debski.calculationservice.clients.ExchangeRatesClient;
import com.debski.calculationservice.enums.CalculationType;
import com.debski.calculationservice.enums.Currency;
import com.debski.calculationservice.exceptions.CalculationException;
import com.debski.calculationservice.models.*;
import one.util.streamex.StreamEx;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class BudgetCalculator {

    private ExchangeRatesClient exchangeRatesClient;

    private MessageSource messageSource;

    public BudgetCalculator(ExchangeRatesClient exchangeRatesClient, MessageSource messageSource) {
        this.exchangeRatesClient = exchangeRatesClient;
        this.messageSource = messageSource;
    }

    public Set<IncomeDTO> filterIncomesByDateTimePeriod(Set<IncomeDTO> incomes, LocalDate startDate, LocalDate endDate) {
        Set<IncomeDTO> filteredIncomes = incomes
                .stream()
                .filter(i -> isWithinStartAndEndDate(i.getDateOfIncome(), startDate, endDate))
                .collect(Collectors.toSet());
        return filteredIncomes;
    }

    public Set<OutcomeDTO> filterOutcomesByDateTimePeriod(Set<OutcomeDTO> outcomes, LocalDate startDate, LocalDate endDate) {
        Set<OutcomeDTO> filteredOutcomes = outcomes
                .stream()
                .filter(o -> isWithinStartAndEndDate(o.getDateOfOutcome(), startDate, endDate))
                .collect(Collectors.toSet());
        return filteredOutcomes;
    }

    private boolean isWithinStartAndEndDate(LocalDate date, LocalDate startDate, LocalDate endDate) {
        return ((date.isEqual(startDate) || date.isAfter(startDate)) && (date.isBefore(endDate) || date.isEqual(endDate)));
    }


    public void adjustIncomesCurrencies(Set<IncomeDTO> incomes, Currency inputCurrency) {
        for (var income : incomes) {
            BigDecimal amountAfterExchange = exchangeCurrencies(income.getAmount(), income.getCurrency(), inputCurrency);
            income.setAmount(amountAfterExchange);
            income.setCurrency(inputCurrency);
        }
    }

    public void adjustOutcomesCurrencies(Set<OutcomeDTO> outcomes, Currency inputCurrency) {
        for (var outcome : outcomes) {
            BigDecimal amountAfterExchange = exchangeCurrencies(outcome.getAmount(), outcome.getCurrency(), inputCurrency);
            outcome.setAmount(amountAfterExchange);
            outcome.setCurrency(inputCurrency);
        }
    }

    private BigDecimal exchangeCurrencies(BigDecimal amount, Currency baseCurrency, Currency inputCurrency) {
        if (baseCurrency.equals(inputCurrency)) {
            return amount.setScale(2, RoundingMode.HALF_UP);
        }
        ExchangeRatesContainer rates = exchangeRatesClient.getRates(baseCurrency);
        validateRates(rates, inputCurrency);
        BigDecimal rate = rates.getRates().get(inputCurrency.name());
        BigDecimal amountAfterExchange = amount.multiply(rate);
        return amountAfterExchange.setScale(2, RoundingMode.HALF_UP);
    }

    private void validateRates(ExchangeRatesContainer rates, Currency inputCurrency) {
        if (rates == null || rates.getRates().get(inputCurrency.name()) == null) {
            throw new CalculationException(messageSource.getMessage("rates.not.found", null, LocaleContextHolder.getLocale()));
        }
    }

    public Set<IncomeDTO> convertIncomesFrequencies(Set<IncomeDTO> filteredIncomes, LocalDate endDate) {
        Set<Set<IncomeDTO>> convertedAndFilteredIncomes = filteredIncomes.stream()
                .map(i -> {
                    Set<IncomeDTO> convertedIncomes = new HashSet<>();
                    switch (i.getFrequency()) {
                        case ONCE: {
                            convertedIncomes.add(i);
                            break;
                        }
                        case DAILY: {
                            for (i.getDateOfIncome(); i.getDateOfIncome().isBefore(endDate.plusDays(1)); i.setDateOfIncome(i.getDateOfIncome().plusDays(1))) {
                                convertedIncomes.add(IncomeDTO.clone(i));
                            }
                            break;
                        }
                        case MONTHLY: {
                            for (i.getDateOfIncome(); i.getDateOfIncome().isBefore(endDate.plusDays(1)); i.setDateOfIncome(i.getDateOfIncome().plusMonths(1))) {
                                convertedIncomes.add(IncomeDTO.clone(i));
                            }
                            break;
                        }
                        case QUARTERLY: {
                            for (i.getDateOfIncome(); i.getDateOfIncome().isBefore(endDate.plusDays(1)); i.setDateOfIncome(i.getDateOfIncome().plusMonths(3))) {
                                convertedIncomes.add(IncomeDTO.clone(i));
                            }
                            break;
                        }
                        case YEARLY: {
                            for (i.getDateOfIncome(); i.getDateOfIncome().isBefore(endDate.plusDays(1)); i.setDateOfIncome(i.getDateOfIncome().plusYears(1))) {
                                convertedIncomes.add(IncomeDTO.clone(i));
                            }
                            break;
                        }
                    }
                    return convertedIncomes;
                })
                .collect(Collectors.toSet());

        Set<IncomeDTO> result = joinAllSets(convertedAndFilteredIncomes);
        return result;
    }

    public Set<OutcomeDTO> convertOutcomesFrequencies(Set<OutcomeDTO> filteredOutcomes, LocalDate endDate) {
        Set<Set<OutcomeDTO>> convertedAndFilteredOutcomes = filteredOutcomes.stream()
                .map(o -> {
                    Set<OutcomeDTO> convertedOutcomes = new HashSet<>();
                    switch (o.getFrequency()) {
                        case ONCE: {
                            convertedOutcomes.add(o);
                            break;
                        }
                        case DAILY: {
                            for (o.getDateOfOutcome(); o.getDateOfOutcome().isBefore(endDate.plusDays(1)); o.setDateOfOutcome(o.getDateOfOutcome().plusDays(1))) {
                                convertedOutcomes.add(OutcomeDTO.clone(o));
                            }
                            break;
                        }
                        case MONTHLY: {
                            for (o.getDateOfOutcome(); o.getDateOfOutcome().isBefore(endDate.plusDays(1)); o.setDateOfOutcome(o.getDateOfOutcome().plusMonths(1))) {
                                convertedOutcomes.add(OutcomeDTO.clone(o));
                            }
                            break;
                        }
                        case QUARTERLY: {
                            for (o.getDateOfOutcome(); o.getDateOfOutcome().isBefore(endDate.plusDays(1)); o.setDateOfOutcome(o.getDateOfOutcome().plusMonths(3))) {
                                convertedOutcomes.add(OutcomeDTO.clone(o));
                            }
                            break;
                        }
                        case YEARLY: {
                            for (o.getDateOfOutcome(); o.getDateOfOutcome().isBefore(endDate.plusDays(1)); o.setDateOfOutcome(o.getDateOfOutcome().plusYears(1))) {
                                convertedOutcomes.add(OutcomeDTO.clone(o));
                            }
                            break;
                        }
                    }
                    return convertedOutcomes;
                })
                .collect(Collectors.toSet());

        Set<OutcomeDTO> result = joinAllSets(convertedAndFilteredOutcomes);
        return result;
    }

    private <T> Set<T> joinAllSets(Set<Set<T>> sets ) {
        Set<T> result = new HashSet<>();
        for (Set<T> set : sets) {
            for (T item : set) {
                result.add(item);
            }
        }
        return result;
    }

    // First implementation of merge function.
//    public CalculationOutput mergeIncomesAndOutcomes(Set<IncomeDTO> incomes, Set<OutcomeDTO> outcomes) {
//        Currency currency = incomes.iterator().next().getCurrency();
//        List<VisualisationPoint> growingVisualisationPoints = incomes.stream()
//                .map(i -> new VisualisationPoint(i.getAmount(), i.getDateOfIncome()))
//                .sorted(Comparator.comparing(VisualisationPoint::getDate))
//                .collect(Collectors.toCollection(ArrayList::new));
//        List<VisualisationPoint> decreasingVisualisationPoints = outcomes.stream()
//                .map(o -> new VisualisationPoint(o.getAmount().negate(), o.getDateOfOutcome()))
//                .sorted(Comparator.comparing(VisualisationPoint::getDate))
//                .collect(Collectors.toCollection(ArrayList::new));
//        List<VisualisationPoint> mergedVPs = merge(growingVisualisationPoints, decreasingVisualisationPoints);
//        CalculationOutput result = CalculationOutput.builder()
//                .calculationType(CalculationType.BOTH)
//                .currency(currency)
////                .visualisationPoints(mergedVPs)
//                .build();
//        return result;
//    }
//
//    private List<VisualisationPoint> merge(List<VisualisationPoint> growingVPs, List<VisualisationPoint> decreasingVPs) {
//        List<VisualisationPoint> result = new ArrayList<>();
//        int gVPsSize = growingVPs.size();
//        int dVPsSize = decreasingVPs.size();
//        int gVPsIdx = 0;
//        int dVPsIdx = 0;
//        int resultIdx = 0;
//
//        while (isAbleToIterateThroughBothLists(growingVPs, gVPsIdx, decreasingVPs, dVPsIdx)) {
//            LocalDate currentGVPDate = growingVPs.get(gVPsIdx).getDate();
//            LocalDate currentDVPDate = decreasingVPs.get(dVPsIdx).getDate();
//            BigDecimal currentGVPAmount = growingVPs.get(gVPsIdx).getValue();
//            BigDecimal currentDVPAmount = decreasingVPs.get(dVPsIdx).getValue();
//            // if currentGVP Date is earlier
//            if (currentGVPDate.compareTo(currentDVPDate) < 0) {
//                // if there is any item in result
//                if (!result.isEmpty()) {
//                    // sum up with amount of last item added to result
//                    BigDecimal amountOfLastResultItem = result.get(resultIdx).getValue();
//                    result.add(new VisualisationPoint(currentGVPAmount.add(amountOfLastResultItem), currentGVPDate));
//                    ++resultIdx;
//                } else {
//                    result.add(growingVPs.get(gVPsIdx));
//                }
//                ++gVPsIdx;
//
//                // if Dates are equal
//            } else if (currentGVPDate.compareTo(currentDVPDate) == 0) {
//                // calculate total amount of currentGVP and currentDVP
//                BigDecimal totalAmount = currentGVPAmount.add(currentDVPAmount);
//                // if there is any item in result
//                if (!result.isEmpty()) {
//                    // sum up with amount of last item added to result
//                    BigDecimal amountOfLastResultItem = result.get(resultIdx).getValue();
//                    result.add(new VisualisationPoint(totalAmount.add(amountOfLastResultItem), currentGVPDate));
//                    ++resultIdx;
//                } else {
//                    result.add(new VisualisationPoint(totalAmount, currentGVPDate));
//                }
//                ++gVPsIdx;
//                ++dVPsIdx;
//
//                // if currentDVP Date is earlier
//            } else {
//                // if there is any item in result
//                if (!result.isEmpty()) {
//                    // sum up with amount of last item added to result
//                    BigDecimal amountOfLastResultItem = result.get(resultIdx).getValue();
//                    result.add(new VisualisationPoint(currentDVPAmount.add(amountOfLastResultItem), currentDVPDate));
//                    ++resultIdx;
//                } else {
//                    result.add(decreasingVPs.get(dVPsIdx));
//                }
//                ++dVPsIdx;
//            }
//        }
//        // check if one of the list wasn't iterated to the end, and if not - add leftovers to result
//        if (gVPsIdx < gVPsSize) {
//            BigDecimal currentGVPAmount = growingVPs.get(gVPsIdx).getValue();
//            LocalDate currentGVPDate = growingVPs.get(gVPsIdx).getDate();
//            // if there is any item in result
//            if (!result.isEmpty()) {
//                // sum up with amount of last item added to result
//                BigDecimal amountOfLastResultItem = result.get(resultIdx).getValue();
//                result.add(new VisualisationPoint(currentGVPAmount.add(amountOfLastResultItem), currentGVPDate));
//                ++resultIdx;
//            } else {
//                result.add(growingVPs.get(gVPsIdx));
//            }
//            ++gVPsIdx;
//        } else if (dVPsIdx < dVPsSize) {
//            BigDecimal currentDVPAmount = decreasingVPs.get(dVPsIdx).getValue();
//            LocalDate currentDVPDate = decreasingVPs.get(dVPsIdx).getDate();
//            // if there is any item in result
//            if (!result.isEmpty()) {
//                // sum up with amount of last item added to result
//                BigDecimal amountOfLastResultItem = result.get(resultIdx).getValue();
//                result.add(new VisualisationPoint(currentDVPAmount.add(amountOfLastResultItem), currentDVPDate));
//                ++resultIdx;
//            } else {
//                result.add(decreasingVPs.get(dVPsIdx));
//            }
//            ++dVPsIdx;
//        }
//
//        return result;
//    }
//
//
//    private boolean isAbleToIterateThroughBothLists (List < VisualisationPoint > growingVPs,int gVPsIdx, List<
//            VisualisationPoint > decreasingVPs,int dVPsIdx){
//        return (gVPsIdx < growingVPs.size() && dVPsIdx < decreasingVPs.size());
//    }

    public CalculationOutput mergeIncomesAndOutcomes(Set<IncomeDTO> incomes, Set<OutcomeDTO> outcomes, Currency currency) {
        // currency is same for every item after exchangeCurrencies method
        Stream<VisualisationPoint> growingVPsStream = incomes.stream()
                .map(i -> new VisualisationPoint(i.getAmount(), i.getDateOfIncome()));
        Stream<VisualisationPoint> decreasingVPsStream = outcomes.stream()
                .map(o -> new VisualisationPoint(o.getAmount().negate(), o.getDateOfOutcome()));
        Stream<VisualisationPoint> mergedStreams = Stream.concat(growingVPsStream, decreasingVPsStream)
                .sorted();
        NavigableSet<VisualisationPoint> mergedVPs = StreamEx.of(mergedStreams)
                .collapse((vp1, vp2) -> vp1.compareTo(vp2) == 0,
                        (vp1, vp2) -> new VisualisationPoint(vp1.getValue().add(vp2.getValue()), vp1.getDate()))
                .collect(Collectors.toCollection(TreeSet::new));
        calculateAmounts(mergedVPs);

        CalculationOutput result = CalculationOutput.builder()
                .calculationType(CalculationType.BOTH)
                .currency(currency)
                .visualisationPoints(mergedVPs)
                .build();
        return result;
    }

    private void calculateAmounts(NavigableSet<VisualisationPoint> mergedVPs) {
        VisualisationPoint iterVp = mergedVPs.first();
        while (mergedVPs.higher(iterVp) != null) {
            VisualisationPoint laterVp = mergedVPs.higher(iterVp);
            laterVp.setValue(laterVp.getValue().add(iterVp.getValue()));
            iterVp = laterVp;
        }
    }
}
