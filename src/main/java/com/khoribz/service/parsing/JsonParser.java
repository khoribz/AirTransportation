package com.khoribz.service.parsing;

public class JsonParser {
    public String parse(String json) {
        String[] jsonPoints = json.split(",");
        return  jsonPoints[0].substring(11, jsonPoints[0].length() - 2);
    }
}
