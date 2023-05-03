package com.example.dataproducer.producer;

import com.example.dataproducer.domain.StockHistoryObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * @author irfan.nagoo
 */

@RequiredArgsConstructor
@Slf4j
public class KafkaMessageProducer implements AsyncMessageProducer<StockHistoryObject> {

    private final KafkaTemplate<Object, Object> kafkaTemplate;
    private final String topic;

    @Override
    public void sentMessage(StockHistoryObject stockHistory) {
        log.info("Sending message with name [{}]", stockHistory.getName());
        kafkaTemplate.send(topic, stockHistory);
        log.info("Message sent successfully");
    }
}
