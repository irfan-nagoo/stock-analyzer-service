package com.example.dataconsumer.config;

import com.example.dataconsumer.consumer.AsyncMessageConsumer;
import com.example.dataconsumer.consumer.KafkaMessageConsumer;
import com.example.dataconsumer.domain.StockHistoryObject;
import com.example.dataconsumer.service.StockHistoryService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.converter.JsonMessageConverter;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.util.backoff.FixedBackOff;

/**
 * @author irfan.nagoo
 */


@Configuration
@ConditionalOnProperty(value = "com.example.data.consumer.message.broker",
        havingValue = "kafka")
public class KafkaConfig {

    @Bean
    public RecordMessageConverter converter() {
        return new JsonMessageConverter();
    }

    @Bean
    public CommonErrorHandler errorHandler(KafkaOperations<Object, Object> kafkaOperations) {
        return new DefaultErrorHandler(new DeadLetterPublishingRecoverer(kafkaOperations)
                , new FixedBackOff(100L, 2));
    }

    @Bean
    public AsyncMessageConsumer<StockHistoryObject> stockHistoryConsumer(StockHistoryService stockHistoryService,
                                                                         TaskExecutor taskExecutor) {
        return new KafkaMessageConsumer(stockHistoryService, taskExecutor);
    }

}
