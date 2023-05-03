package com.example.dataproducer.config;

import com.example.dataproducer.domain.StockHistoryObject;
import com.example.dataproducer.producer.AsyncMessageProducer;
import com.example.dataproducer.producer.KafkaMessageProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.core.KafkaTemplate;
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
@ConditionalOnProperty(value = "com.example.data.producer.message.broker",
        havingValue = "kafka")
public class KafkaConfig {

    @Value("${com.example.data.producer.kafka.topic}")
    private String stockTopic;

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
    public AsyncMessageProducer<StockHistoryObject> stockHistoryProducer(KafkaTemplate<Object, Object> kafkaTemplate) {
        return new KafkaMessageProducer(kafkaTemplate, stockTopic);
    }

}
