package com.noetic.sgw.mtgateway.rabbitmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.noetic.sgw.mtgateway.util.MtProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;

import static com.noetic.sgw.mtgateway.util.Constants.HIGH_PRIORITY_QUEUE;
import static com.noetic.sgw.mtgateway.util.Constants.LOW_PRIORITY_QUEUE;
import static com.noetic.sgw.mtgateway.util.Constants.MEDIUM_PRIORITY_QUEUE;
import static com.noetic.sgw.mtgateway.util.Constants.NOETIC_QUEUE;

@Component
public class QueueProducer implements Serializable {
    private String fanoutExchange = "mtExchange";
    private final RabbitTemplate rabbitTemplate;
    @Autowired
    public QueueProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
    public void produceMt(MtProperties mtProperties) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        System.out.println(mtProperties.getData());
        if (mtProperties.getData().contains("Muzammil")) {
            rabbitTemplate.convertAndSend("exchange", "routingKey", objectMapper.writeValueAsString(mtProperties));
            System.out.println("NOETIC_QUEUE: " + mtProperties.getData().contains("Muzammil"));
        } else if (mtProperties.getData().contains("OTP") || mtProperties.getData().contains("Verification") || mtProperties.getData().contains("PIN")) {
            System.out.println("HIGH_PRIORITY_QUEUE: " + (mtProperties.getData().contains("OTP") || mtProperties.getData().contains("Verification") || mtProperties.getData().contains("PIN")));
            rabbitTemplate.convertAndSend("exchange", "routingKey1", objectMapper.writeValueAsString(mtProperties));
        } else if (mtProperties.getData().contains("Successful registration") || mtProperties.getData().contains("Welcome to Mind+!") || mtProperties.getData().contains("subscribed") || mtProperties.getData().contains("unsubscribe")) {
            System.out.println("MEDIUM_PRIORITY_QUEUE: " + (mtProperties.getData().contains("Successful registration") || mtProperties.getData().contains("Welcome to Mind+!") || mtProperties.getData().contains("subscribed") || mtProperties.getData().contains("unsubscribe")));
            rabbitTemplate.convertAndSend("exchange", "routingKey2", objectMapper.writeValueAsString(mtProperties));
        } else {
            System.out.println("LOW_PRIORITY_QUEUE: " + mtProperties.getData());
            rabbitTemplate.convertAndSend("exchange", "routingKey3", objectMapper.writeValueAsString(mtProperties));
        }
    }
}