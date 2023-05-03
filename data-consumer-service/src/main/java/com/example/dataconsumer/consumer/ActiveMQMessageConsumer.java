package com.example.dataconsumer.consumer;

import com.example.dataconsumer.domain.StockHistoryObject;
import com.example.dataconsumer.service.StockHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jms.annotation.JmsListener;

/**
 * @author irfan.nagoo
 */

@RequiredArgsConstructor
@Slf4j
public class ActiveMQMessageConsumer implements AsyncMessageConsumer<StockHistoryObject> {

    private final StockHistoryService stockHistoryService;
    private final TaskExecutor taskExecutor;

    @Override
    @JmsListener(destination = "${com.example.data.consumer.activemq.queue}",
            containerFactory = "messageListenerContainer")
    public void receiveMessage(StockHistoryObject stockHistory) {
        log.info("New Message received with name [{}]", stockHistory.getName());
        taskExecutor.execute(() -> stockHistoryService.processMessage(stockHistory));
    }
}
