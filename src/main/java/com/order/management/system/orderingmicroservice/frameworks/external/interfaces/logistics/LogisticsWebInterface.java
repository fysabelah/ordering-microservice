package com.order.management.system.orderingmicroservice.frameworks.external.interfaces.logistics;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("tracking-microservice")
public interface LogisticsWebInterface {

    @DeleteMapping("/{orderId}")
    JsonNode cancelDelivery(@PathVariable String orderId);

    @PostMapping("/{cep}/{orderId}")
    JsonNode createDelivery(@PathVariable String orderId, @PathVariable String cep, @RequestBody JsonNode items);
}
