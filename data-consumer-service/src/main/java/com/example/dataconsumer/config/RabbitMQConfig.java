package com.example.dataconsumer.config;

import com.example.dataconsumer.consumer.AsyncMessageConsumer;
import com.example.dataconsumer.consumer.RabbitMQMessageConsumer;
import com.example.dataconsumer.domain.StockHistoryObject;
import com.example.dataconsumer.service.StockHistoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;

/**
 * @author irfan.nagoo
 */

@Configuration
@ConditionalOnProperty(value = "com.example.data.consumer.message.broker",
        havingValue = "rabbitmq")
public class RabbitMQConfig {

    @Value("${com.example.data.consumer.rabbitmq.queue}")
    private String queueName;

    @Value("${com.example.data.consumer.rabbitmq.exchange}")
    private String topicExchange;

    @Value("${com.example.data.consumer.rabbitmq.routing-key}")
    private String routingKey;

    @Bean
    public Queue queue() {
        return new Queue(queueName, false);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(topicExchange);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange topicExchange) {
        return BindingBuilder.bind(queue).to(topicExchange)
                .with(routingKey);
    }

    @Bean
    public MessageConverter converter(ObjectMapper objectMapper) {
        objectMapper.registerModule(new JavaTimeModule());
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public AsyncMessageConsumer<StockHistoryObject> stockHistoryConsumer(StockHistoryService stockHistoryService,
                                                                         TaskExecutor taskExecutor) {
        return new RabbitMQMessageConsumer(stockHistoryService, taskExecutor);
    }


}
