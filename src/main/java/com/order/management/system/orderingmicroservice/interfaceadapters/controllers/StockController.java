package com.order.management.system.orderingmicroservice.interfaceadapters.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.order.management.system.orderingmicroservice.entities.Order;
import com.order.management.system.orderingmicroservice.frameworks.external.messaging.inventory.StockProduceMessage;
import com.order.management.system.orderingmicroservice.interfaceadapters.gateways.OrderGateway;
import com.order.management.system.orderingmicroservice.interfaceadapters.presenters.dtos.ItemDto;
import com.order.management.system.orderingmicroservice.util.MessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StockController {

    @Autowired
    private OrderGateway orderGateway;

    @Autowired
    private StockProduceMessage produceMessage;

    private static final Logger LOGGER = LoggerFactory.getLogger(StockController.class);

    public void cancelReservation(Integer orderId) throws JsonProcessingException {
        Order order = orderGateway.findByWithItems(orderId);

        produceMessage.sendMessageUpdateStock(orderId, order.getItems());
    }

    public void sendMessageConfirmReservation(List<ItemDto> items, Integer orderId) throws JsonProcessingException {
        LOGGER.info(MessageUtil.getMessage("LOG_MESSAGE_VALIDATE_STOCK", orderId.toString()));

        produceMessage.sendMessageProcessStock(items, orderId);
    }
}
