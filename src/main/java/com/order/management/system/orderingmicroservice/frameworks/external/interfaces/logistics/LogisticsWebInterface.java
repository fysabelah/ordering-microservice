package com.order.management.system.orderingmicroservice.frameworks.external.interfaces.logistics;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient("tracking-microservice")
public interface LogisticsWebInterface {

    @DeleteMapping(value = "/tracking")
    void cancelDelivery(@RequestParam String orderId);

    @PostMapping(value = "/tracking")
    void createDelivery(@RequestParam String cep, @RequestParam String order, @RequestBody JsonNode items);
}
