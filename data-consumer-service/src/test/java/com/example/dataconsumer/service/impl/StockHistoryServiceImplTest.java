package com.example.dataconsumer.service.impl;

import com.example.dataconsumer.entity.StockHistory;
import com.example.dataconsumer.exception.RecordNotFoundException;
import com.example.dataconsumer.repository.StockHistoryRepository;
import com.example.dataconsumer.response.StockHistoryResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockHistoryServiceImplTest {

    @Mock
    private StockHistoryRepository stockHistoryRepository;

    @InjectMocks
    private StockHistoryServiceImpl stockHistoryService;


    @Test
    void getStockHistoryByTicker() {
        StockHistory stockHistory = new StockHistory();
        stockHistory.setId("12345");
        stockHistory.setName("APPL");
        stockHistory.setDate(LocalDate.now());
        stockHistory.setClosePrice(BigDecimal.TEN);
        when(stockHistoryRepository.findByName(anyString(), any()))
                .thenReturn(Collections.singletonList(stockHistory));
        StockHistoryResponse response = stockHistoryService.getStockHistoryByTicker("APPL", 0, 10, "date", "asc");
        assertEquals(HttpStatus.OK.name(), response.getCode());
        assertEquals(1, response.getStockHistory().size());
        assertEquals("APPL", response.getStockHistory().get(0).getName());
    }

    @Test
    void getStockHistoryByTicker_NotFound() {
        when(stockHistoryRepository.findByName(anyString(), any()))
                .thenReturn(Collections.emptyList());
        RecordNotFoundException response = assertThrows(RecordNotFoundException.class,
                () -> stockHistoryService.getStockHistoryByTicker("APPL", 0, 10, "date", "asc"));
        assertEquals("The data not found for ticker [APPL]", response.getMessage());
    }

    @Test
    void getStockHistoryByDateRange() {
        StockHistory stockHistory = new StockHistory();
        stockHistory.setId("12345");
        stockHistory.setName("APPL");
        stockHistory.setDate(LocalDate.now());
        stockHistory.setClosePrice(BigDecimal.TEN);
        when(stockHistoryRepository.findByNameAndDateBetween(anyString(), any(), any(), any()))
                .thenReturn(Collections.singletonList(stockHistory));
        StockHistoryResponse response = stockHistoryService.getStockHistoryByDateRange("APPL", LocalDate.MIN, LocalDate.MAX, "date", "asc");
        assertEquals(HttpStatus.OK.name(), response.getCode());
        assertEquals(1, response.getStockHistory().size());
        assertEquals("APPL", response.getStockHistory().get(0).getName());
    }

    @Test
    void deleteByNameAndBDateRange() {
        assertDoesNotThrow(() -> stockHistoryService.deleteByNameAndBDateRange("APPL", LocalDate.MIN, LocalDate.MAX));
    }

    @Test
    void processMessage() {
        StockHistory stockHistory = new StockHistory();
        stockHistory.setId("12345");
        stockHistory.setName("APPL");
        stockHistory.setDate(LocalDate.now());
        stockHistory.setClosePrice(BigDecimal.TEN);
        when(stockHistoryRepository.save(any())).thenReturn(stockHistory);
        assertDoesNotThrow(() -> stockHistoryService.processMessage(stockHistory.toObject()));
    }
}