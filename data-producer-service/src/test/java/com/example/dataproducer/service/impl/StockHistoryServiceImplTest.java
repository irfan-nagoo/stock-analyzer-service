package com.example.dataproducer.service.impl;

import com.example.dataproducer.domain.StockHistoryObject;
import com.example.dataproducer.feign.StockHistoryFeignClient;
import com.example.dataproducer.producer.AsyncMessageProducer;
import com.example.dataproducer.request.StockHistoryRequest;
import com.example.dataproducer.response.BaseResponse;
import com.example.dataproducer.response.StockHistoryResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;

import static com.example.dataproducer.constants.MessagingConstants.CSV_FILE_REQUIRED_ERROR_MSG;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockHistoryServiceImplTest {

    @Mock
    private StockHistoryFeignClient feignClient;

    @Mock
    private AsyncMessageProducer<StockHistoryObject> messageProducer;

    @InjectMocks
    private StockHistoryServiceImpl stockHistoryService;


    @Test
    void getStockHistoryByTicker() {
        StockHistoryObject stockHistory = getStockHistory();
        StockHistoryResponse mockResponse = new StockHistoryResponse(HttpStatus.OK.name(), "Some message");
        mockResponse.setStockHistory(Collections.singletonList(stockHistory));
        when(feignClient.getStockHistoryByName(anyString(), anyInt(), anyInt(), anyString(), anyString()))
                .thenReturn(mockResponse);

        StockHistoryResponse response = stockHistoryService.getStockHistoryByTicker("APPL", 0, 100,
                "date", "asc");
        assertNotNull(response);
        assertEquals(HttpStatus.OK.name(), response.getCode());
        assertEquals(1, response.getStockHistory().size());
        assertEquals("APPL", response.getStockHistory().get(0).getName());
    }

    @Test
    void processStockHistory() {
        doNothing().when(messageProducer).sentMessage(any());
        StockHistoryObject stockHistory = getStockHistory();
        StockHistoryRequest request = new StockHistoryRequest();
        request.setStockHistory(stockHistory);
        BaseResponse response = stockHistoryService.processStockHistory(request);
        assertNotNull(response);
        assertEquals(HttpStatus.OK.name(), response.getCode());
    }

    @Test
    void processBulkStockHistory() {
        MockMultipartFile file = new MockMultipartFile("APPL.csv", "Some Record".getBytes());
        BaseResponse response = stockHistoryService.processBulkStockHistory("APPL", Collections.singletonList(file));
        assertNotNull(response);
        assertEquals(HttpStatus.OK.name(), response.getCode());
    }

    @Test
    void processBulkStockHistory_Exception() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> stockHistoryService.processBulkStockHistory("APPL", Collections.emptyList()));
        assertNotNull(exception);
        assertEquals(CSV_FILE_REQUIRED_ERROR_MSG, exception.getMessage());
    }


    @Test
    void deleteByNameAndBDateRange() {
        assertDoesNotThrow(() -> stockHistoryService.deleteByNameAndBDateRange("APPL", LocalDate.MIN, LocalDate.MAX));
    }


    private static StockHistoryObject getStockHistory() {
        StockHistoryObject stockHistory = new StockHistoryObject();
        stockHistory.setId("12345");
        stockHistory.setName("APPL");
        stockHistory.setDate(LocalDate.now());
        stockHistory.setClosePrice(BigDecimal.TEN);
        return stockHistory;
    }
}