package com.example.dataproducer.controller;

import com.example.dataproducer.domain.StockHistoryObject;
import com.example.dataproducer.response.BaseResponse;
import com.example.dataproducer.response.StockHistoryResponse;
import com.example.dataproducer.service.StockHistoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    void processStockHistory() throws Exception {
        when(stockHistoryService.processStockHistory(any())).thenReturn(
                new BaseResponse(HttpStatus.OK.name(), "Some message"));
        StockHistoryObject stockHistory = new StockHistoryObject();
        String stockHistoryJson = new ObjectMapper().writeValueAsString(stockHistory);
        mockMvc.perform(post("/stock-history/ticker/APPL/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(stockHistoryJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(HttpStatus.OK.name())));
    }

    @Test
    void processBulkStockHistory() throws Exception {
        when(stockHistoryService.processBulkStockHistory(any(), anyList())).thenReturn(
                new BaseResponse(HttpStatus.OK.name(), "Some message"));
        mockMvc.perform(multipart("/stock-history/ticker/APPL/bulk-save")
                        .file(new MockMultipartFile("files", "Some Records".getBytes())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(HttpStatus.OK.name())));
    }

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
    void deleteByNameAndBDateRange() throws Exception {
        when(stockHistoryService.deleteByNameAndBDateRange(anyString(), any(), any())).thenReturn(
                new StockHistoryResponse(HttpStatus.OK.name(), "Some message"));
        mockMvc.perform(delete("/stock-history/ticker/APPL/delete")
                        .param("startDate", "2022-01-01")
                        .param("endDate", "2022-12-31"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(HttpStatus.OK.name())));
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