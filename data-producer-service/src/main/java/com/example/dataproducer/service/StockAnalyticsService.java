package com.example.dataproducer.service;

import com.example.dataproducer.response.StockAnalyticsResponse;
import com.example.dataproducer.response.StockAnalyticsTradeResponse;

import java.time.LocalDate;

/**
 * @author irfan.nagoo
 */
public interface StockAnalyticsService {

    StockAnalyticsResponse getStockAnalytics(String ticker, LocalDate startDate,
                                             LocalDate endDate);

    StockAnalyticsTradeResponse getStockAnalyticsTrade(String ticker, int month);
}
