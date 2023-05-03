package com.example.dataproducer.config;

import com.example.dataproducer.domain.StockHistoryObject;
import com.example.dataproducer.producer.ActiveMQMessageProducer;
import com.example.dataproducer.producer.AsyncMessageProducer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import java.util.HashMap;
import java.util.Map;

/**
 * @author irfan.nagoo
 */

@Configuration
@ConditionalOnProperty(value = "com.example.data.producer.message.broker",
        havingValue = "activemq")
public class ActiveMQConfig {

    @Value("${spring.activemq.brokerUrl}")
    private String brokerUrl;

    @Value("${com.example.data.producer.activemq.queue}")
    private String queue;

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

    public JmsTemplate jmsTemplate(ActiveMQConnectionFactory connectionFactory,
                                   MessageConverter messageConverter) {
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(connectionFactory);
        jmsTemplate.setMessageConverter(messageConverter);
        return jmsTemplate;
    }

    @Bean
    public AsyncMessageProducer<StockHistoryObject> stockHistoryProducer(JmsTemplate jmsTemplate) {
        return new ActiveMQMessageProducer(jmsTemplate, queue);
    }

}
