package com.order.management.system.orderingmicroservice.frameworks.external.interfaces.product;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("product-microservice")
interface InventoryWebInterface {

    @PutMapping(value = "/reservation/confirmation", consumes = "application/json")
    JsonNode confirmReservation(@RequestBody JsonNode reservation);

    @PutMapping(value = "/reservation/cancelation", consumes = "application/json")
    JsonNode cancelReservation(@RequestBody JsonNode reservation);
}
