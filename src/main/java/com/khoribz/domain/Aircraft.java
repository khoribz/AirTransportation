package com.khoribz.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Aircraft {
    private final String aircraftCode;
    private final String model;
    private final Integer range;
}