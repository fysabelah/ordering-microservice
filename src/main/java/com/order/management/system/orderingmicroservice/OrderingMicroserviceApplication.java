package com.order.management.system.orderingmicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class OrderingMicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderingMicroserviceApplication.class, args);
    }
}
