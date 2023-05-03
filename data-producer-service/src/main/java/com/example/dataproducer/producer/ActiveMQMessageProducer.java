package com.example.dataproducer.producer;

import com.example.dataproducer.domain.StockHistoryObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;

/**
 * @author irfan.nagoo
 */

@RequiredArgsConstructor
@Slf4j
public class ActiveMQMessageProducer implements AsyncMessageProducer<StockHistoryObject> {

    private final JmsTemplate jmsTemplate;
    private final String queue;

    @Override
    public void sentMessage(StockHistoryObject stockHistory) {
        log.info("Sending message with name [{}]", stockHistory.getName());
        jmsTemplate.convertAndSend(queue, stockHistory);
        log.info("Message sent successfully");
    }
}
