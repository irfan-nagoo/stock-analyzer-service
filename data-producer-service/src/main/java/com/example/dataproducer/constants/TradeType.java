package com.example.dataproducer.constants;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author irfan.nagoo
 */
public enum TradeType {

    BUY("Buy"),
    SELL("Sell"),
    UNKNOWN("Unknown");

    @JsonValue
    private String value;

    TradeType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
