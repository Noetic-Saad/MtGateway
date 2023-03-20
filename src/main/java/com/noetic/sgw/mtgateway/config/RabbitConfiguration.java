package com.noetic.sgw.mtgateway.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.noetic.sgw.mtgateway.util.Constants.HIGH_PRIORITY_QUEUE;
import static com.noetic.sgw.mtgateway.util.Constants.LOW_PRIORITY_QUEUE;
import static com.noetic.sgw.mtgateway.util.Constants.MEDIUM_PRIORITY_QUEUE;
import static com.noetic.sgw.mtgateway.util.Constants.NOETIC_QUEUE;

@Configuration
@EnableRabbit
public class RabbitConfiguration {

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public Queue queue() {
        return new Queue(NOETIC_QUEUE, true);
    }
    @Bean
    public Queue queue1() {
        return new Queue(HIGH_PRIORITY_QUEUE, true);
    }
    @Bean
    public Queue queue2() {
        return new Queue(MEDIUM_PRIORITY_QUEUE, true);
    }
    @Bean
    public Queue queue3() {
        return new Queue(LOW_PRIORITY_QUEUE, true);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange("exchange");
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue()).to(exchange()).with("routingKey");
    }

    @Bean
    public Binding binding1() {
        return BindingBuilder.bind(queue1()).to(exchange()).with("routingKey1");
    }

    @Bean
    public Binding binding2() {
        return BindingBuilder.bind(queue2()).to(exchange()).with("routingKey2");
    }
    @Bean
    public Binding binding3() {
        return BindingBuilder.bind(queue3()).to(exchange()).with("routingKey3");
    }
    @Bean
    public RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry() {
        return new RabbitListenerEndpointRegistry();
    }
}