package com.example.dataconsumer.consumer;

import com.example.dataconsumer.domain.StockHistoryObject;
import com.example.dataconsumer.service.StockHistoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.task.TaskExecutor;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(MockitoExtension.class)
class ActiveMQMessageConsumerTest {

    @Mock
    private StockHistoryService stockHistoryService;

    @Mock
    private TaskExecutor taskExecutor;

    AsyncMessageConsumer<StockHistoryObject> consumer;

    @Test
    void receiveMessage() {
        StockHistoryObject stockHistory = new StockHistoryObject();
        stockHistory.setId("12345");
        stockHistory.setName("APPL");
        stockHistory.setDate(LocalDate.now());
        stockHistory.setClosePrice(BigDecimal.TEN);
        consumer = new ActiveMQMessageConsumer(stockHistoryService, taskExecutor);
        assertDoesNotThrow(() -> consumer.receiveMessage(stockHistory));
    }
}