package com.order.management.system.orderingmicroservice.interfaceadapters.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.order.management.system.orderingmicroservice.frameworks.external.messaging.logistics.LogisticsMessaging;
import com.order.management.system.orderingmicroservice.interfaceadapters.gateways.OrderGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LogisticsController {

    @Autowired
    private LogisticsMessaging logisticsMessaging;

    @Autowired
    private OrderGateway orderGateway;

    public void cancelDelivery(Integer orderId) throws JsonProcessingException {
        orderGateway.findById(orderId);

        logisticsMessaging.sendMessageCancelDelivery(orderId);
    }

    public void prepareDelivery(Integer orderId) throws JsonProcessingException {
        orderGateway.findById(orderId);

        logisticsMessaging.sendToCarrier(orderId);
    }
}
