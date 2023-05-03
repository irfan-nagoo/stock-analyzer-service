package com.example.dataproducer.constants;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author irfan.nagoo
 */
public enum StockPerformanceType {
    EXCELLENT("Excellent"),
    VERY_GOOD("Very Good"),
    GOOD("Good"),
    AVERAGE("Average"),
    BAD("Bad");

    @JsonValue
    private String value;

    StockPerformanceType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
