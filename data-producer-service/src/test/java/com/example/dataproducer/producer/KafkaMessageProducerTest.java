package com.example.dataproducer.producer;

import com.example.dataproducer.domain.StockHistoryObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


@ExtendWith(MockitoExtension.class)
class KafkaMessageProducerTest {

    @Mock
    private KafkaTemplate<Object, Object> kafkaTemplate;

    AsyncMessageProducer<StockHistoryObject> producer;

    @Test
    void sentMessage() {
        StockHistoryObject stockHistory = new StockHistoryObject();
        stockHistory.setId("12345");
        stockHistory.setName("APPL");
        stockHistory.setDate(LocalDate.now());
        stockHistory.setClosePrice(BigDecimal.TEN);
        producer = new KafkaMessageProducer(kafkaTemplate, "testTopic");
        assertDoesNotThrow(() -> producer.sentMessage(stockHistory));
    }
}