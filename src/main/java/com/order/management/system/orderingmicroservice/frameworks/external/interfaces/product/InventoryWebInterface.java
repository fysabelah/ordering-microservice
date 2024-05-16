package com.order.management.system.orderingmicroservice.frameworks.external.interfaces.product;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient("inventory-microservice")
interface InventoryWebInterface {

    @PutMapping(value = "/reservation/confirmation")
    ArrayNode confirmReservation(ArrayNode reservation);

    @PutMapping(value = "/reservation/cancelation")
    ArrayNode cancelReservation(ArrayNode reservation);
}
