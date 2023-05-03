package com.example.dataproducer.service.impl;

import com.example.dataproducer.constants.StockPerformanceType;
import com.example.dataproducer.constants.TradeType;
import com.example.dataproducer.domain.StockHistoryObject;
import com.example.dataproducer.feign.StockHistoryFeignClient;
import com.example.dataproducer.response.StockAnalyticsResponse;
import com.example.dataproducer.response.StockAnalyticsTradeResponse;
import com.example.dataproducer.response.StockHistoryResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StockAnalyticsServiceImplTest {

    @Mock
    private StockHistoryFeignClient feignClient;

    @InjectMocks
    private StockAnalyticsServiceImpl stockAnalyticsService;

    @Test
    void getStockAnalytics() {
        StockHistoryObject stockHistory1 = getStockHistory();
        stockHistory1.setDate(LocalDate.parse("2023-05-01"));
        stockHistory1.setClosePrice(BigDecimal.valueOf(75));
        stockHistory1.setVolume(10000L);
        StockHistoryObject stockHistory2 = getStockHistory();
        stockHistory2.setDate(LocalDate.parse("2023-05-02"));
        stockHistory2.setClosePrice(BigDecimal.valueOf(80));
        stockHistory2.setVolume(20000L);
        StockHistoryObject stockHistory3 = getStockHistory();
        stockHistory3.setDate(LocalDate.parse("2023-05-03"));
        stockHistory3.setClosePrice(BigDecimal.valueOf(90));
        stockHistory3.setVolume(30000L);
        StockHistoryResponse mockResponse = new StockHistoryResponse(HttpStatus.OK.name(), "Some message");
        mockResponse.setStockHistory(Arrays.asList(stockHistory1, stockHistory2, stockHistory3));
        when(feignClient.getStockHistoryByDateRange(anyString(), any(), any(), anyString(), anyString()))
                .thenReturn(mockResponse);

        StockAnalyticsResponse response = stockAnalyticsService.getStockAnalytics("APPL", LocalDate.MIN, LocalDate.MAX);
        assertNotNull(response);
        assertEquals("100.00", response.getStockAnalytics().getPeriodHighPrice().toString());
        assertEquals("50.00", response.getStockAnalytics().getPeriodLowPrice().toString());
        assertEquals("250000.00", response.getStockAnalytics().getInitialMarketCap().toString());
        assertEquals("81.67", response.getStockAnalytics().getPeriodAveragePrice().toString());
        assertEquals(StockPerformanceType.EXCELLENT.getValue(), response.getStockAnalytics().getStockPerformance().getValue());
    }

    @Test
    void getStockAnalyticsTrade() {
        StockHistoryObject stockHistory1 = getStockHistory();
        stockHistory1.setDate(LocalDate.parse("2023-05-01"));
        stockHistory1.setClosePrice(BigDecimal.valueOf(75));
        stockHistory1.setVolume(10000L);
        StockHistoryObject stockHistory2 = getStockHistory();
        stockHistory2.setDate(LocalDate.parse("2023-05-02"));
        stockHistory2.setClosePrice(BigDecimal.valueOf(80));
        stockHistory2.setVolume(20000L);
        StockHistoryObject stockHistory3 = getStockHistory();
        stockHistory3.setDate(LocalDate.parse("2023-05-03"));
        stockHistory3.setClosePrice(BigDecimal.valueOf(77));
        stockHistory3.setVolume(17000L);
        StockHistoryResponse mockResponse = new StockHistoryResponse(HttpStatus.OK.name(), "Some message");
        mockResponse.setStockHistory(Arrays.asList(stockHistory1, stockHistory2, stockHistory3));
        when(feignClient.getStockHistoryByName(anyString(), anyInt(), anyInt(), anyString(), anyString()))
                .thenReturn(mockResponse);

        StockAnalyticsTradeResponse response = stockAnalyticsService.getStockAnalyticsTrade("APPL", 5);
        assertNotNull(response);
        assertEquals(HttpStatus.OK.name(), response.getCode());
        assertEquals(3000L, response.getStockAnalyticsTrade().getMaxBuyVolume());
        assertEquals(10000L, response.getStockAnalyticsTrade().getMaxSellVolume());
        assertEquals("50.00%", response.getStockAnalyticsTrade().getAvgBuyPercent());
        assertEquals("50.00%", response.getStockAnalyticsTrade().getAvgSellPercent());
        assertEquals(TradeType.BUY.getValue(), response.getStockAnalyticsTrade().getMajorityTradeAction().getValue());
    }

    private static StockHistoryObject getStockHistory() {
        StockHistoryObject stockHistory = new StockHistoryObject();
        stockHistory.setId("12345");
        stockHistory.setName("APPL");
        stockHistory.setOpenPrice(BigDecimal.valueOf(25));
        stockHistory.setLowPrice(BigDecimal.valueOf(50));
        stockHistory.setHighPrice(BigDecimal.valueOf(100));
        return stockHistory;
    }
}