package com.example.dataconsumer.config;

import com.example.dataconsumer.consumer.ActiveMQMessageConsumer;
import com.example.dataconsumer.consumer.AsyncMessageConsumer;
import com.example.dataconsumer.domain.StockHistoryObject;
import com.example.dataconsumer.service.StockHistoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import java.util.HashMap;
import java.util.Map;

/**
 * @author irfan.nagoo
 */

@Configuration
@ConditionalOnProperty(value = "com.example.data.consumer.message.broker",
        havingValue = "activemq")
@Slf4j
@EnableJms
public class ActiveMQConfig {

    @Value("${spring.activemq.brokerUrl}")
    private String brokerUrl;

    @Bean
    public ActiveMQConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        connectionFactory.setBrokerURL(brokerUrl);
        return connectionFactory;
    }

    @Bean
    public MessageConverter converter(ObjectMapper objectMapper) {
        objectMapper.registerModule(new JavaTimeModule());
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setObjectMapper(objectMapper);
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        Map<String, Class<?>> typeIdMapping = new HashMap<>();
        typeIdMapping.put("StockHistory", StockHistoryObject.class);
        converter.setTypeIdMappings(typeIdMapping);
        return converter;
    }

    @Bean
    public DefaultJmsListenerContainerFactory messageListenerContainer(ActiveMQConnectionFactory connectionFactory,
                                                                       MessageConverter converter) {
        DefaultJmsListenerContainerFactory jmsListenerContainerFactory = new DefaultJmsListenerContainerFactory();
        jmsListenerContainerFactory.setConnectionFactory(connectionFactory);
        jmsListenerContainerFactory.setConcurrency("1-1");
        jmsListenerContainerFactory.setMessageConverter(converter);
        jmsListenerContainerFactory.setErrorHandler(e -> log.error("Error occurred: ", e));
        return jmsListenerContainerFactory;
    }

    @Bean
    public AsyncMessageConsumer<StockHistoryObject> stockHistoryConsumer(StockHistoryService stockHistoryService,
                                                                         TaskExecutor taskExecutor) {
        return new ActiveMQMessageConsumer(stockHistoryService, taskExecutor);
    }

}
