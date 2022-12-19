package com.khoribz.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@AllArgsConstructor
@Data
public class Booking {
    private final String bookRef;
    private final Timestamp bookDate;
    private final BigDecimal totalAmount;
}