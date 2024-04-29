package com.order.management.system.orderingmicroservice.interfaceadapters.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.order.management.system.orderingmicroservice.entities.Order;
import com.order.management.system.orderingmicroservice.entities.StatusHistory;
import com.order.management.system.orderingmicroservice.frameworks.external.messaging.ClientMessaging;
import com.order.management.system.orderingmicroservice.frameworks.external.messaging.PaymentMessaging;
import com.order.management.system.orderingmicroservice.frameworks.external.messaging.StockMessaging;
import com.order.management.system.orderingmicroservice.interfaceadapters.gateways.OrderGateway;
import com.order.management.system.orderingmicroservice.interfaceadapters.presenters.OrderPresenter;
import com.order.management.system.orderingmicroservice.interfaceadapters.presenters.dtos.Dto;
import com.order.management.system.orderingmicroservice.interfaceadapters.presenters.dtos.OrderDto;
import com.order.management.system.orderingmicroservice.usercase.OrderUserCase;
import com.order.management.system.orderingmicroservice.util.MessageUtil;
import com.order.management.system.orderingmicroservice.util.enums.OrderCancellationType;
import com.order.management.system.orderingmicroservice.util.enums.OrderStatus;
import com.order.management.system.orderingmicroservice.util.exception.BusinessException;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Component
public class OrderController {

    @Resource
    private OrderUserCase business;

    @Resource
    private OrderGateway gateway;

    @Resource
    private OrderPresenter presenter;

    @Resource
    private ClientMessaging clientMessaging;

    @Resource
    private StockMessaging stockMessaging;

    @Resource
    private PaymentMessaging paymentMessaging;

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

    public OrderDto insert(Dto dto) throws JsonProcessingException {
        Order order = business.createOrder(dto);

        order = gateway.insert(order);

        LOGGER.info(MessageUtil.getMessage("LOG_MESSAGE_ORDER_CREATED", order.getId().toString()));

        LOGGER.info(MessageUtil.getMessage("LOG_MESSAGE_VALIDATE_CLIENT", order.getId().toString(), order.getClient().getDocument()));
        clientMessaging.sendMessage(order);

        LOGGER.info(MessageUtil.getMessage("LOG_MESSAGE_VALIDATE_STOCK", order.getId().toString()));
        stockMessaging.sendMessageProcessStock(order.getItems(), order.getId());

        LOGGER.info(MessageUtil.getMessage("LOG_MESSAGE_VALIDATE_PAYMENT", order.getId().toString()));
        paymentMessaging.sendMessage(dto.getPayment(), order.getId());

        return presenter.convert(order);
    }

    public OrderDto findById(Integer id) {
        Order order = gateway.findById(id);

        return presenter.convert(order);
    }

    public void cancel(Integer orderId, String document, OrderCancellationType motive) throws JsonProcessingException {
        Optional<Order> optional = gateway.findByIdAndClientDocument(orderId, document);

        if (optional.isEmpty()) {
            throw new NoSuchElementException(MessageUtil.getMessage("MESSAGE_ID_NOT_FOUND", "Pedido", orderId.toString()));
        }

        Order order = optional.get();

        if (order.getStatusHistory() != null && hasStock(order.getStatusHistory())) {
            LOGGER.info(MessageUtil.getMessage("LOG_MESSAGE_SEND_STOCK_UPDATE_ALREADY_SEPARATED"));
            stockMessaging.sendMessageCancelReservation(order.getId(), order.getItems());
        }

        LOGGER.info(MessageUtil.getMessage("LOG_MESSAGE_UPDATING_ORDER", order.getId().toString(), "cancelamento"));

        business.updateToCancel(order, motive);

        gateway.update(order);

        LOGGER.info(MessageUtil.getMessage("LOG_MESSAGE_CANCEL_ORDER", order.getId().toString(), order.getMotive().toString()));
    }

    private boolean hasStock(List<StatusHistory> statusHistories) {
        return statusHistories.stream()
                .anyMatch(statusHistory -> OrderStatus.STOCK_SEPARATION.compareTo(statusHistory.getStatus()) == 0);
    }

    public void updateToOnCarrier(Integer id, String trackingNumber) throws BusinessException {
        Order order = gateway.findById(id);

        LOGGER.info(MessageUtil.getMessage("LOG_MESSAGE_UPDATING_ORDER", order.getId().toString(), "atualização de status"));

        business.updateToOnCarrier(order, trackingNumber);

        gateway.update(order);

        LOGGER.info(MessageUtil.getMessage("LOG_MESSAGE_UPDATE_ORDER", order.getId().toString(), OrderStatus.SHIPPING_READY.toString(), OrderStatus.ON_CARRIAGE.name()));
    }
}
