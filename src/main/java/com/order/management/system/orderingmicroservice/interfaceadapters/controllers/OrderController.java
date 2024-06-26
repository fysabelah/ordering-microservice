package com.order.management.system.orderingmicroservice.interfaceadapters.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.order.management.system.orderingmicroservice.entities.Order;
import com.order.management.system.orderingmicroservice.frameworks.external.messaging.client.ClientPublishMessage;
import com.order.management.system.orderingmicroservice.frameworks.external.messaging.payment.PaymentPublishMessaging;
import com.order.management.system.orderingmicroservice.frameworks.external.messaging.inventory.StockProduceMessage;
import com.order.management.system.orderingmicroservice.interfaceadapters.gateways.OrderGateway;
import com.order.management.system.orderingmicroservice.interfaceadapters.presenters.OrderPresenter;
import com.order.management.system.orderingmicroservice.interfaceadapters.presenters.dtos.Dto;
import com.order.management.system.orderingmicroservice.interfaceadapters.presenters.dtos.OrderDto;
import com.order.management.system.orderingmicroservice.usercase.OrderUserCase;
import com.order.management.system.orderingmicroservice.util.MessageUtil;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderController {

    @Resource
    private OrderUserCase business;

    @Resource
    private OrderGateway gateway;

    @Resource
    private OrderPresenter presenter;

    @Resource
    private ClientPublishMessage clientPublishMessage;

    @Resource
    private StockProduceMessage stockProduceMessage;

    @Resource
    private PaymentPublishMessaging paymentPublishMessaging;

    @Autowired
    private StockController stockController;

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

    public OrderDto insert(Dto dto) throws JsonProcessingException {
        Order order = business.createOrder(dto);

        order = gateway.insert(order);

        LOGGER.info(MessageUtil.getMessage("LOG_MESSAGE_ORDER_CREATED", order.getId().toString()));

        LOGGER.info(MessageUtil.getMessage("LOG_MESSAGE_VALIDATE_CLIENT", order.getId().toString(), order.getClient().getDocument()));
        clientPublishMessage.sendMessage(order);

        stockController.sendMessageConfirmReservation(dto.getItems(), order.getId());

        LOGGER.info(MessageUtil.getMessage("LOG_MESSAGE_VALIDATE_PAYMENT", order.getId().toString()));
        paymentPublishMessaging.sendMessage(dto.getPayment(), order.getId());

        return presenter.convert(order);
    }

    public OrderDto findById(Integer id) {
        Order order = gateway.findById(id);

        return presenter.convert(order);
    }
}
