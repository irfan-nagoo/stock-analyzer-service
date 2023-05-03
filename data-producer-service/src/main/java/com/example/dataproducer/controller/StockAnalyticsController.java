package com.example.dataproducer.controller;

import com.example.dataproducer.response.StockAnalyticsResponse;
import com.example.dataproducer.response.StockAnalyticsTradeResponse;
import com.example.dataproducer.service.StockAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * @author irfan.nagoo
 */

@RestController
@RequestMapping("/stock-analytics")
@RequiredArgsConstructor
public class StockAnalyticsController {

    private final StockAnalyticsService stockAnalyticsService;

    /**
     * Returns the Stock analytical statistics for a given date range.
     *
     * @param ticker    Stock symbol
     * @param startDate Period start date
     * @param endDate   Period end date
     * @return Stock analytical statistics
     */
    @GetMapping("/ticker/{ticker}/report")
    public StockAnalyticsResponse getStockAnalytics(@PathVariable("ticker") String ticker,
                                                    @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                    @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return stockAnalyticsService.getStockAnalytics(ticker, startDate, endDate);
    }

    /**
     * Returns the Stock trading statistics for a given month using the stock historical data
     * present in the database.
     *
     * @param ticker Stock Symbol
     * @param month  Month to be evaluated for trade statistics
     * @return Stock trading statistics
     */
    @GetMapping("/ticker/{ticker}/trade")
    public StockAnalyticsTradeResponse getStockAnalyticsTrade(@PathVariable("ticker") String ticker,
                                                              @RequestParam("month") int month) {
        return stockAnalyticsService.getStockAnalyticsTrade(ticker, month);
    }
}
