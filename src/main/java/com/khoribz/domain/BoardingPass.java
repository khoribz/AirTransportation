package com.khoribz.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class BoardingPass {
    private final String ticketNo;
    private final Integer flightId;
    private final Integer boardingNo;
    private final String seatNo;
}