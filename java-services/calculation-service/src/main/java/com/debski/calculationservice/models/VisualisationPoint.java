package com.debski.calculationservice.models;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class VisualisationPoint implements Comparable<VisualisationPoint>{

    private BigDecimal value;

    private LocalDate date;

    @Override
    public int compareTo(VisualisationPoint o) {
        return this.date.compareTo(o.getDate());
    }
}
