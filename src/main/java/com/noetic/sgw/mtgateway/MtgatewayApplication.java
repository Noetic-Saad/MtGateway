package com.noetic.sgw.mtgateway;

import com.noetic.sgw.mtgateway.services.MtService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableEurekaClient
public class MtgatewayApplication implements CommandLineRunner {

    private final MtService mtService;

    public MtgatewayApplication(MtService mtService) {
        this.mtService = mtService;
    }

    public static void main(String[] args) {
        SpringApplication.run(MtgatewayApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        mtService.consumeQueue();
    }
}
