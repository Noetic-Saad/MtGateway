package com.noetic.sgw.mtgateway.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.noetic.sgw.mtgateway.services.MtService;
import com.noetic.sgw.mtgateway.util.MtProperties;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;

import static com.noetic.sgw.mtgateway.util.Constants.HIGH_PRIORITY_QUEUE;
import static com.noetic.sgw.mtgateway.util.Constants.LOW_PRIORITY_QUEUE;
import static com.noetic.sgw.mtgateway.util.Constants.MEDIUM_PRIORITY_QUEUE;
import static com.noetic.sgw.mtgateway.util.Constants.NOETIC_QUEUE;

@Component
public class QueueConsumer {

    private final ObjectMapper mapper;
    public static Logger log = LoggerFactory.getLogger(MtService.class);
    private final RabbitTemplate rabbitTemplate;


    public QueueConsumer(ObjectMapper mapper, RabbitTemplate rabbitTemplate) {
        this.mapper = mapper;
        this.rabbitTemplate = rabbitTemplate;
    }
    public Object noeticQueueReceiveMessage() {
        Object message = rabbitTemplate.receiveAndConvert(NOETIC_QUEUE);
        System.out.println("NOETIC_QUEUE: " + message);
        return message;
    }
    public Object highPriorityQueueReceiveMessage() {
        Object message = rabbitTemplate.receiveAndConvert(HIGH_PRIORITY_QUEUE);
        System.out.println("HIGH_PRIORITY_QUEUE: " + message);
        return message;
    }
    public Object mediumPriorityQueueReceiveMessage() {
        Object message = rabbitTemplate.receiveAndConvert(MEDIUM_PRIORITY_QUEUE);
        System.out.println("MEDIUM_PRIORITY_QUEUE: " + message);
        return message;
    }
    public Object lowPriorityQueueReceiveMessage() {
        Object message = rabbitTemplate.receiveAndConvert(LOW_PRIORITY_QUEUE);
        System.out.println("LOW_PRIORITY_QUEUE: " + message);
        return message;
    }
    public void queueConsumer(Object rabbitMQMessage) throws InterruptedException {
        String jsonString = "{" + rabbitMQMessage.toString().substring(rabbitMQMessage.toString().indexOf("{") +
                1, rabbitMQMessage.toString().indexOf("}")) + "}";
        System.out.println(jsonString);
        try {
            MtProperties mtProperties = mapper.readValue(jsonString, MtProperties.class);
            log.info(URLEncoder.encode("https://node.noeticworld.com/sdp/interface/mt_gateway?username" +
                    "= " + mtProperties.getUsername() + " &password= " + mtProperties.getPassword() + " " +
                    "&shortcode= " + mtProperties.getShortCode() + " &serviceid= " + mtProperties
                    .getServiceId() + " &msisdn= " + mtProperties.getMsisdn() + " &data= "
                    + mtProperties.getData(), "UTF-8"));
            String message = URLEncoder.encode(mtProperties.getData(), "UTF-8");
            HttpResponse<String> response = Unirest
                    .get("https://node.noeticworld.com/sdp/interface/mt_gateway?username="
                            + mtProperties.getUsername() + "&password=" + mtProperties.getPassword()
                            + "&shortcode=" + mtProperties.getShortCode() + "&serviceid=" + mtProperties
                            .getServiceId() + "&msisdn=" + mtProperties.getMsisdn() + "&data=" + message)
                    .header("cache-control", "no-cache")
                    .header("postman-token", "b705b706-9526-845c-2f73-2868ddc02bd2")
                    .asString();
            if (response.getStatus() == 200) {
                System.out.println("++++++++SENT++++++++ response.getStatus() == 200");
            } else
            {
                System.out.println("++++++++ELSE++++++++ response.getStatus() != 200");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}