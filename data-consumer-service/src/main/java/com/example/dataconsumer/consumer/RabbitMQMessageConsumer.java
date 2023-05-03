package com.example.dataconsumer.consumer;

import com.example.dataconsumer.domain.StockHistoryObject;
import com.example.dataconsumer.service.StockHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.core.task.TaskExecutor;

/**
 * @author irfan.nagoo
 */

@RequiredArgsConstructor
@Slf4j
public class RabbitMQMessageConsumer implements AsyncMessageConsumer<StockHistoryObject> {

    private final StockHistoryService stockHistoryService;
    private final TaskExecutor taskExecutor;

    @Override
    @RabbitListener(queues = "${com.example.data.consumer.rabbitmq.queue}")
    public void receiveMessage(StockHistoryObject stockHistory) {
        log.info("New Message received with name [{}]", stockHistory.getName());
        taskExecutor.execute(() -> stockHistoryService.processMessage(stockHistory));
    }
}
