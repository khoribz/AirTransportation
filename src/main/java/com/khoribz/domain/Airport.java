package com.khoribz.domain;

import java.awt.Point;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Airport {
    private final String airportCode;
    private final String airportName;
    private final String city;
    private final String coordinates;
    private final String timezone;
}