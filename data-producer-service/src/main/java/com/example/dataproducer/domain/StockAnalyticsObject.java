package com.example.dataproducer.domain;

import com.example.dataproducer.constants.StockPerformanceType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author irfan.nagoo
 */

@Getter
@Setter
public class StockAnalyticsObject {

    private BigDecimal periodOpenPrice;
    private BigDecimal periodClosePrice;
    private BigDecimal periodLowPrice;
    private LocalDate periodLowPriceDate;
    private BigDecimal periodHighPrice;
    private LocalDate periodHighPriceDate;
    private BigDecimal periodAveragePrice;
    private StockPerformanceType stockPerformance;
    private String pricePercentChange;
    private BigDecimal initialMarketCap;
    private BigDecimal latestMarketCap;

}
