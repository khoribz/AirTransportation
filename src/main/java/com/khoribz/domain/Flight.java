package com.khoribz.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

@AllArgsConstructor
@Data
public class Flight {
    private final Integer flightId;
    private final String flightNo;
    private final Timestamp scheduledDeparture;
    private final Timestamp scheduledArrival;
    private final String departureAirport;
    private final String arrivalAirport;
    private final String status;
    private final String aircraftCode;
    private final Timestamp actualDeparture;
    private final Timestamp actualArrival;
}