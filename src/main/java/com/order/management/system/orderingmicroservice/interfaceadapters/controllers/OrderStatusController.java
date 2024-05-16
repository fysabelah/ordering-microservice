package com.order.management.system.orderingmicroservice.interfaceadapters.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.order.management.system.orderingmicroservice.entities.Order;
import com.order.management.system.orderingmicroservice.frameworks.external.messaging.StockMessaging;
import com.order.management.system.orderingmicroservice.frameworks.external.messaging.TransportationMessaging;
import com.order.management.system.orderingmicroservice.interfaceadapters.gateways.OrderGateway;
import com.order.management.system.orderingmicroservice.usercase.OrderStatusUserCase;
import com.order.management.system.orderingmicroservice.util.MessageUtil;
import com.order.management.system.orderingmicroservice.util.enums.OrderCancellationType;
import com.order.management.system.orderingmicroservice.util.enums.OrderStatus;
import com.order.management.system.orderingmicroservice.util.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderStatusController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderStatusController.class);

    @Autowired
    private OrderGateway orderGateway;

    @Autowired
    private OrderStatusUserCase userCase;

    @Autowired
    private StockMessaging stockMessaging;

    @Autowired
    private TransportationMessaging transportationMessaging;

    public void updateStatus(OrderStatus status, Integer orderId) {
        Order order = orderGateway.findByIdWithStatusHistory(orderId);

        OrderStatus old = order.getStatus();

        LOGGER.info(MessageUtil.getMessage("LOG_MESSAGE_UPDATING_ORDER", order.getId().toString(), "atualização de status"));

        if (userCase.validateStatusMap(order, status)) {
            orderGateway.update(order);

            LOGGER.info(MessageUtil.getMessage("LOG_MESSAGE_UPDATE_ORDER", order.getId().toString(), old.name(), order.getStatus().name()));
        }
    }

    public void updateStatus(Integer orderId, String tracking, String urlTracking) throws BusinessException {
        Order order = orderGateway.findById(orderId);

        LOGGER.info(MessageUtil.getMessage("LOG_MESSAGE_UPDATING_ORDER", order.getId().toString(), "atualização de status"));

        userCase.updateToOnCarrier(order, tracking, urlTracking);

        orderGateway.update(order);

        LOGGER.info(MessageUtil.getMessage("LOG_MESSAGE_UPDATE_ORDER", order.getId().toString(), OrderStatus.SHIPPED.toString(), OrderStatus.ON_CARRIAGE.name()));
    }

    public void cancel(Integer orderId, OrderCancellationType cancellationType) throws JsonProcessingException {
        Order order = orderGateway.findById(orderId);

        if (userCase.hasStock(order.getStatusHistory())) {
            LOGGER.info(MessageUtil.getMessage("LOG_MESSAGE_SEND_STOCK_UPDATE_ALREADY_SEPARATED"));

            stockMessaging.sendMessageUpdateStock(order.getId(), order.getItems());
        }

        if (userCase.hasTransportantionHistory(order.getStatusHistory())) {
            LOGGER.info(MessageUtil.getMessage("LOG_MESSAGE_CANCEL_ORDER_DELIVERY", orderId.toString()));

            transportationMessaging.sendMessageCancelDelivery(orderId);
        }

        LOGGER.info(MessageUtil.getMessage("LOG_MESSAGE_UPDATING_ORDER", order.getId().toString(), "cancelamento"));

        userCase.updateStatusCanceled(order, cancellationType);

        orderGateway.update(order);

        LOGGER.info(MessageUtil.getMessage("LOG_MESSAGE_CANCEL_ORDER", order.getId().toString(), order.getMotive().toString()));
    }
}
