package com.order.management.system.orderingmicroservice.frameworks.external.interfaces.client;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient("customers-microservice")
interface ClientWebInterface {

    @GetMapping(value = "/customer/document/{document}")
    JsonNode findByDocument(@PathVariable String document);

    @PostMapping(value = "/customer")
    void insert(JsonNode client);

}
