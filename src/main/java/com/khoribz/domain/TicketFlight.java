package com.khoribz.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@AllArgsConstructor
@Data
public class TicketFlight {
    private final String ticketNo;
    private final Integer flightId;
    private final String fareConditions;
    private final BigDecimal amount;
}