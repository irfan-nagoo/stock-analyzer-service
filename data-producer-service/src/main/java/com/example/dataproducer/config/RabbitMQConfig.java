package com.example.dataproducer.config;

import com.example.dataproducer.domain.StockHistoryObject;
import com.example.dataproducer.producer.AsyncMessageProducer;
import com.example.dataproducer.producer.RabbitMQMessageProducer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author irfan.nagoo
 */

@Configuration
@ConditionalOnProperty(value = "com.example.data.producer.message.broker",
        havingValue = "rabbitmq")
public class RabbitMQConfig {

    @Value("${com.example.data.producer.rabbitmq.exchange}")
    private String topicExchange;

    @Value("${com.example.data.producer.rabbitmq.routing-key}")
    private String routingKey;

    @Bean
    public MessageConverter converter(ObjectMapper objectMapper) {
        objectMapper.registerModule(new JavaTimeModule());
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         MessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }

    @Bean
    public AsyncMessageProducer<StockHistoryObject> stockHistoryProducer(RabbitTemplate rabbitTemplate) {
        return new RabbitMQMessageProducer(rabbitTemplate, topicExchange, routingKey);
    }


}
