package com.example.dataproducer.controller;

import com.example.dataproducer.response.StockAnalyticsResponse;
import com.example.dataproducer.response.StockAnalyticsTradeResponse;
import com.example.dataproducer.service.StockAnalyticsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = StockAnalyticsController.class)
class StockAnalyticsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StockAnalyticsService stockAnalyticsService;

    @Test
    void getStockAnalytics() throws Exception {
        when(stockAnalyticsService.getStockAnalytics(anyString(), any(), any())).thenReturn(
                new StockAnalyticsResponse(HttpStatus.OK.name(), "Some message"));
        mockMvc.perform(get("/stock-analytics/ticker/APPL/report")
                        .param("startDate", "2000-01-01")
                        .param("endDate", "2023-05-31"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(HttpStatus.OK.name())));
    }

    @Test
    void getStockAnalyticsTrade() throws Exception {
        when(stockAnalyticsService.getStockAnalyticsTrade(anyString(), anyInt())).thenReturn(
                new StockAnalyticsTradeResponse(HttpStatus.OK.name(), "Some message"));
        mockMvc.perform(get("/stock-analytics/ticker/APPL/trade")
                        .param("month", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(HttpStatus.OK.name())));
    }
}