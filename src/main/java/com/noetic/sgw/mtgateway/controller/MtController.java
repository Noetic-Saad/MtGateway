package com.noetic.sgw.mtgateway.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.noetic.sgw.mtgateway.rabbitmq.QueueConsumer;
import com.noetic.sgw.mtgateway.responses.ApiResponse;
import com.noetic.sgw.mtgateway.services.MtService;
import com.noetic.sgw.mtgateway.util.MtProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.Date;

@RestController
public class MtController {
    private final QueueConsumer queueConsumer;
    private final MtService mtService;
    private Integer count = 0;
    public MtController(QueueConsumer queueConsumer, MtService mtService) {
        this.queueConsumer = queueConsumer;
        this.mtService = mtService;
    }
    @RequestMapping(value = "/mt", method = RequestMethod.POST)
    public ResponseEntity<?> sendMt(@RequestBody MtProperties properties) throws UnsupportedEncodingException, JsonProcessingException {
        mtService.mtSender(properties);

        return new ResponseEntity<>(new ApiResponse("Message stored successfully into " +
                "RabbitMQ", true, new Date()), HttpStatus.CREATED);
    }

//    @RequestMapping(value = "/consume-mt", method = RequestMethod.GET)
//    public void consumeMt(@RequestBody MtProperties properties) throws UnsupportedEncodingException, JsonProcessingException, InterruptedException {
//        queueConsumer.receivedMessage(properties);
////        return new ResponseEntity<>(new ApiResponse("Message consumes successfully",
////                true, new Date()), HttpStatus.CREATED);
//    }

    @RequestMapping(value = "/sendbulkmt", method = RequestMethod.POST)
    public void sendbulkMt(@RequestBody MtProperties properties) throws UnsupportedEncodingException, JsonProcessingException {
        mtService.mtBulkSender(properties);
    }
    @RequestMapping(value = "/otp-verification", method = RequestMethod.POST)
    public void verifyOtp(@RequestBody MtProperties properties) throws UnsupportedEncodingException, JsonProcessingException {
        mtService.mtSender(properties);
    }
}
