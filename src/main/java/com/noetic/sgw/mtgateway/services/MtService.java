package com.noetic.sgw.mtgateway.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.noetic.sgw.mtgateway.rabbitmq.QueueConsumer;
import com.noetic.sgw.mtgateway.rabbitmq.QueueProducer;
import com.noetic.sgw.mtgateway.util.Constants;
import com.noetic.sgw.mtgateway.util.MtProperties;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.QueueInformation;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

import static com.noetic.sgw.mtgateway.util.Constants.HIGH_PRIORITY_QUEUE;
import static com.noetic.sgw.mtgateway.util.Constants.LOW_PRIORITY_QUEUE;
import static com.noetic.sgw.mtgateway.util.Constants.MEDIUM_PRIORITY_QUEUE;
import static com.noetic.sgw.mtgateway.util.Constants.NOETIC_QUEUE;

@Service
public class MtService {

    public static Logger log = LoggerFactory.getLogger(MtService.class.getName());
    private final QueueProducer queueProducer;
    private final RabbitAdmin rabbitAdmin;
    private final QueueConsumer consumer;


    private static final String SDP_URL = "http://210.56.27.18/sdp/interface/mt_gateway";

    public MtService(QueueProducer queueProducer, RabbitAdmin rabbitAdmin, QueueConsumer consumer) {
        this.queueProducer = queueProducer;
        this.rabbitAdmin = rabbitAdmin;
        this.consumer = consumer;
    }



    public void consumeQueue() throws InterruptedException {
        while (true) {
            QueueInformation noeticQueueInfo = rabbitAdmin.getQueueInfo(NOETIC_QUEUE);
            QueueInformation highPriorityQueueInfo = rabbitAdmin.getQueueInfo(HIGH_PRIORITY_QUEUE);
            QueueInformation mediumPriorityQueueInfo = rabbitAdmin.getQueueInfo(MEDIUM_PRIORITY_QUEUE);
            QueueInformation lowPriorityQueueInfo = rabbitAdmin.getQueueInfo(LOW_PRIORITY_QUEUE);
            assert noeticQueueInfo != null;
            if (noeticQueueInfo.getMessageCount() != 0) {
                Object message = consumer.noeticQueueReceiveMessage();
                consumer.queueConsumer(message);
            } else {
                assert highPriorityQueueInfo != null;
                if (highPriorityQueueInfo.getMessageCount() != 0) {
                    Object message = consumer.highPriorityQueueReceiveMessage();
                    consumer.queueConsumer(message);
                } else {
                    assert mediumPriorityQueueInfo != null;
                    if (mediumPriorityQueueInfo.getMessageCount() != 0) {
                        Object message = consumer.mediumPriorityQueueReceiveMessage();
                        consumer.queueConsumer(message);
                    } else {
                        if(lowPriorityQueueInfo.getMessageCount() != 0)
                        {
                            Object message = consumer.lowPriorityQueueReceiveMessage();
                            consumer.queueConsumer(message);
                        }
                    }
                }
            }
        }

    }
    public void mtSender(MtProperties mtProperties) throws UnsupportedEncodingException, JsonProcessingException {
        queueProducer.produceMt(mtProperties);



//        log.info(URLEncoder.encode("http://192.168.127.57:8081/sdp/interface/mt_gateway?username" +
//                "= " + mtProperties.getUsername() + " &password= " + mtProperties.getPassword() + " " +
//                "&shortcode= " + mtProperties.getShortCode() + " &serviceid= " + mtProperties
//                .getServiceId() + " &msisdn= "+mtProperties.getMsisdn()+" &data= "
//                + mtProperties.getData(), "UTF-8"));
//        String message = URLEncoder.encode(mtProperties.getData(),"UTF-8");
//        HttpResponse<String> response = Unirest
//                .get("http://192.168.127.57:8081/sdp/interface/mt_gateway?username="
//                        + mtProperties.getUsername() + "&password=" + mtProperties.getPassword()
//                        + "&shortcode=" + mtProperties.getShortCode() + "&serviceid=" + mtProperties
//                        .getServiceId() + "&msisdn="+mtProperties.getMsisdn()+"&data=" + message)
//                .header("cache-control", "no-cache")
//                .header("postman-token", "b705b706-9526-845c-2f73-2868ddc02bd2")
//                .asString();
//        if (response.getStatus() == 200) {
//            System.out.println("sent");
//        }
    }
    public static void mtBulkSender(MtProperties mtProperties) throws UnsupportedEncodingException {
        System.out.println("In Bulk Mt Sender");
        /*

        System.out.println(URLEncoder.encode("http://192.168.127.57:8081/sdp/interface/mt_gateway?username=" + mtProperties.getUsername() + "&password=" + mtProperties.getPassword() + "&shortcode=" + mtProperties.getShortCode() + "&serviceid=" + mtProperties.getServiceId() + "&msisdn="+mtProperties.getMsisdn()+"&data=" + mtProperties.getData(), "UTF-8"));
        String message = URLEncoder.encode(mtProperties.getData(),"UTF-8");
        HttpResponse<String> response = Unirest.get("http://192.168.127.57:8081/sdp/interface/mt_gateway?username=" + mtProperties.getUsername() + "&password=" + mtProperties.getPassword() + "&shortcode=" + mtProperties.getShortCode() + "&serviceid=" + mtProperties.getServiceId() + "&msisdn="+mtProperties.getMsisdn()+"&data=" + message)
                .header("cache-control", "no-cache")
                .header("postman-token", "b705b706-9526-845c-2f73-2868ddc02bd2")
                .asString();
        if (response.getStatus() == 200) {
            System.out.println("sent");
        }

         */
    }

    public static void telenorOtp(MtProperties mtProperties) {

        HttpResponse<String> response = Unirest.get("http://192.168.127.57:8081/sdp/interface/mt_gateway?username=" + mtProperties.getUsername() + "&password=" + mtProperties.getPassword() + "&shortcode=" + mtProperties.getShortCode() + "&serviceid=" + mtProperties.getServiceId() + "&msisdn=923456846426&data=" + mtProperties.getData())
                .header("cache-control", "no-cache")
                .header("postman-token", "b705b706-9526-845c-2f73-2868ddc02bd2")
                .asString();
        if (response.getStatus() == 200) {
            System.out.println("sent");
        }
    }
    public static void jazzOtp(MtProperties mtProperties) {
        HttpResponse<String> response = Unirest.get("http://192.168.127.57:8081/sdp/interface/mt_gateway?username=" + mtProperties.getUsername() + "&password=" + mtProperties.getPassword() + "&shortcode=" + mtProperties.getShortCode() + "&serviceid=" + mtProperties.getServiceId() + "&msisdn=923456846426&data=" + mtProperties.getData())
                .header("cache-control", "no-cache")
                .header("postman-token", "b705b706-9526-845c-2f73-2868ddc02bd2")
                .asString();
        if (response.getStatus() == 200) {
            System.out.println("sent");
        }
    }
    public static void zongOtp(MtProperties mtProperties) {
        HttpResponse<String> response = Unirest.get("http://192.168.127.57:8081/sdp/interface/mt_gateway?username=" + mtProperties.getUsername() + "&password=" + mtProperties.getPassword() + "&shortcode=" + mtProperties.getShortCode() + "&serviceid=" + mtProperties.getServiceId() + "&msisdn="+mtProperties.getMsisdn()+"&data=" + mtProperties.getData())
                .header("cache-control", "no-cache")
                .header("postman-token", "b705b706-9526-845c-2f73-2868ddc02bd2")
                .asString();
        if (response.getStatus() == 200) {
            System.out.println("sent");
        }
    }
}

