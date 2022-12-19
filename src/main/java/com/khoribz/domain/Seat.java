package com.khoribz.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Seat {
    private final String aircraftCode;
    private final String seatNo;
    private final String fareConditions;
}