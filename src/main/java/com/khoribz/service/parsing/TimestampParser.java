package com.khoribz.service.parsing;

public class TimestampParser {
    public String parse(String timestamp) {
        return timestamp.substring(0, timestamp.length() - 3);
    }
}
