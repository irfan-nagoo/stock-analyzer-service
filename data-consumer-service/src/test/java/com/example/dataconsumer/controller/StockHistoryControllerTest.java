package com.example.dataconsumer.controller;

import com.example.dataconsumer.exception.RecordNotFoundException;
import com.example.dataconsumer.response.BaseResponse;
import com.example.dataconsumer.response.StockHistoryResponse;
import com.example.dataconsumer.service.StockHistoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = StockHistoryController.class)
class StockHistoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StockHistoryService stockHistoryService;

    @Test
    void getStockHistoryByTicker() throws Exception {
        when(stockHistoryService.getStockHistoryByTicker(anyString(), anyInt(), anyInt(), anyString(), anyString())).thenReturn(
                new StockHistoryResponse(HttpStatus.OK.name(), "Some message"));
        mockMvc.perform(get("/stock-history/ticker/APPL/list")
                        .param("pageNo", "1")
                        .param("pageSize", "2")
                        .param("sortBy", "name")
                        .param("sortDirection", "asc"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(HttpStatus.OK.name())));
    }

    @Test
    void getStockHistoryByTicker_NotFound() throws Exception {
        when(stockHistoryService.getStockHistoryByTicker(anyString(), anyInt(), anyInt(), anyString(), anyString()))
                .thenThrow(new RecordNotFoundException("Some error message"));
        mockMvc.perform(get("/stock-history/ticker/APPL/list")
                        .param("pageNo", "1")
                        .param("pageSize", "2")
                        .param("sortBy", "name")
                        .param("sortDirection", "asc"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString(HttpStatus.NOT_FOUND.name())));
    }

    @Test
    void getStockHistoryByDateRange() throws Exception {
        when(stockHistoryService.getStockHistoryByDateRange(anyString(), any(), any(), anyString(), anyString())).thenReturn(
                new StockHistoryResponse(HttpStatus.OK.name(), "Some message"));
        mockMvc.perform(get("/stock-history/ticker/APPL/date")
                        .param("startDate", "2022-01-01")
                        .param("endDate", "2022-12-31")
                        .param("sortBy", "name")
                        .param("sortDirection", "asc"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(HttpStatus.OK.name())));
    }

    @Test
    void deleteByNameAndBDateRange() throws Exception {
        when(stockHistoryService.deleteByNameAndBDateRange(anyString(), any(), any())).thenReturn(
                new BaseResponse(HttpStatus.OK.name(), "Some message"));
        mockMvc.perform(delete("/stock-history/ticker/APPL/delete")
                        .param("startDate", "2022-01-01")
                        .param("endDate", "2022-12-31"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(HttpStatus.OK.name())));
    }
}