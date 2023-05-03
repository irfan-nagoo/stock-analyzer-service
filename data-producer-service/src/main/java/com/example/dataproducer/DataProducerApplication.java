package com.example.dataproducer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class DataProducerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataProducerApplication.class, args);
    }

}
