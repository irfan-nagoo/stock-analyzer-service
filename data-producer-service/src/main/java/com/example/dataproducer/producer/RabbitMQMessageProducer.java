package com.example.dataproducer.producer;

import com.example.dataproducer.domain.StockHistoryObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * @author irfan.nagoo
 */

@RequiredArgsConstructor
@Slf4j
public class RabbitMQMessageProducer implements AsyncMessageProducer<StockHistoryObject> {

    private final RabbitTemplate rabbitTemplate;
    private final String exchange;
    private final String routingKey;

    @Override
    public void sentMessage(StockHistoryObject stockHistory) {
        log.info("Sending message with name [{}]", stockHistory.getName());
        rabbitTemplate.convertAndSend(exchange, routingKey, stockHistory);
        log.info("Message sent successfully");
    }
}
