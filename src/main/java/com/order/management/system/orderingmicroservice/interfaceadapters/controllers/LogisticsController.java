package com.order.management.system.orderingmicroservice.interfaceadapters.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.order.management.system.orderingmicroservice.entities.Order;
import com.order.management.system.orderingmicroservice.frameworks.external.messaging.logistics.LogisticsProduceMessaging;
import com.order.management.system.orderingmicroservice.interfaceadapters.gateways.OrderGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LogisticsController {

    @Autowired
    private LogisticsProduceMessaging logisticsProduceMessaging;

    @Autowired
    private OrderGateway orderGateway;

    public void cancelDelivery(Integer orderId) throws JsonProcessingException {
        Order order = orderGateway.findById(orderId);

        logisticsProduceMessaging.sendMessageCancelDelivery(orderId, order.getItems(), order.getShipment());
    }

    public void prepareDelivery(Integer orderId) throws JsonProcessingException {
        Order order = orderGateway.findByWithItems(orderId);

        logisticsProduceMessaging.sendToCarrier(orderId, order.getItems(), order.getShipment());
    }
}
