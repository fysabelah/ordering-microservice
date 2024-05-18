package com.order.management.system.orderingmicroservice.interfaceadapters.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.order.management.system.orderingmicroservice.entities.Order;
import com.order.management.system.orderingmicroservice.entities.StatusHistory;
import com.order.management.system.orderingmicroservice.frameworks.external.messaging.status.StatusPublishMessage;
import com.order.management.system.orderingmicroservice.interfaceadapters.gateways.OrderGateway;
import com.order.management.system.orderingmicroservice.usercase.OrderStatusUserCase;
import com.order.management.system.orderingmicroservice.util.enums.OrderCancellationType;
import com.order.management.system.orderingmicroservice.util.enums.OrderStatus;
import com.order.management.system.orderingmicroservice.util.enums.PaymentStatus;
import com.order.management.system.orderingmicroservice.util.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static com.order.management.system.orderingmicroservice.util.MessageUtil.getMessage;

@Component
public class OrderStatusController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderStatusController.class);

    @Autowired
    private OrderGateway orderGateway;

    @Autowired
    private OrderStatusUserCase userCase;

    @Autowired
    private StockController stockController;

    @Autowired
    private LogisticsController logisticsController;

    @Autowired
    private StatusPublishMessage statusPublishMessage;

    public void updateToOnCarrier(Integer orderId, String tracking, String urlTracking) throws BusinessException {
        Order order = orderGateway.findByIdWithStatusHistory(orderId);

        LOGGER.info(getMessage("LOG_MESSAGE_UPDATING_ORDER", order.getId().toString(), "atualização de status"));

        userCase.updateToOnCarrier(order, tracking, urlTracking);

        orderGateway.update(order);

        LOGGER.info(getMessage("LOG_MESSAGE_UPDATE_ORDER", order.getId().toString(), OrderStatus.SHIPPED.toString(), OrderStatus.ON_CARRIAGE.name()));
    }

    public void updateToDelivered(Integer orderId) throws BusinessException {
        Order order = orderGateway.findByIdWithStatusHistory(orderId);

        LOGGER.info(getMessage("LOG_MESSAGE_UPDATING_ORDER", order.getId().toString(), "atualização de status"));

        userCase.updateToDelivered(order);

        LOGGER.info(getMessage("LOG_MESSAGE_UPDATE_ORDER", order.getId().toString(), OrderStatus.ON_CARRIAGE.toString(), OrderStatus.DELIVERED.name()));
    }

    private void updateToCancel(Order order, OrderCancellationType motive, LocalDateTime processAt) throws JsonProcessingException, BusinessException {
        userCase.updateStatusCanceled(order, motive, processAt);

        if (userCase.hasStock(order.getStatusHistory())) {
            LOGGER.info(getMessage("LOG_MESSAGE_SEND_STOCK_UPDATE_ALREADY_SEPARATED"));

            stockController.cancelReservation(order.getId());
        }

        if (userCase.hasTransportantionHistory(order.getStatusHistory())) {
            LOGGER.info(getMessage("LOG_MESSAGE_CANCEL_ORDER_DELIVERY", order.getId().toString()));

            logisticsController.cancelDelivery(order.getId());
        }

        LOGGER.info(getMessage("LOG_MESSAGE_UPDATING_ORDER", order.getId().toString(), "cancelamento"));

        if (OrderCancellationType.PAYMENT_FAILURE.equals(motive)) {
            order.getPayment().setStatus(PaymentStatus.UNAUTHORIZED);
        }

        orderGateway.update(order);

        LOGGER.info(getMessage("LOG_MESSAGE_CANCEL_ORDER", order.getId().toString(), order.getMotive().toString()));
    }

    public boolean updateStatus(Integer orderId, OrderStatus status, OrderCancellationType motive, LocalDateTime processAt) throws JsonProcessingException, BusinessException {
        Order order = orderGateway.findByIdWithStatusHistory(orderId);

        if (OrderStatus.CANCELED.equals(status) && !OrderStatus.CANCELED.equals(order.getStatus())) {
            updateToCancel(order, motive, processAt);

            return true;
        }

        if (OrderStatus.CANCELED.equals(order.getStatus())) {
            updateStatusHistory(order, status, processAt, motive);

            return true;
        }

        boolean processed = userCase.validateStatusMap(order, status);

        if (processed) {
            orderGateway.update(order);

            if (OrderStatus.PAYMENT_ACCEPT.equals(status)) {
                logisticsController.prepareDelivery(order.getId());

                statusPublishMessage.sendMessage(orderId, OrderStatus.SHIPPING_READY);
            }
        }

        if (userCase.moveToWaitingPayment(order.getStatusHistory(), status)) {
            statusPublishMessage.sendMessage(orderId, OrderStatus.WAITING_PAYMENT);
        }

        return processed;
    }

    private void updateStatusHistory(Order order, OrderStatus status, LocalDateTime processAt, OrderCancellationType motive) throws JsonProcessingException {
        if (OrderStatus.PAYMENT_ACCEPT.equals(status)) {
            order.getPayment().setStatus(PaymentStatus.AUTHORIZED);
        }

        if (OrderStatus.STOCK_SEPARATION.equals(status)) {
            stockController.cancelReservation(order.getId());
        }

        if (userCase.hasTransportantionHistory(status)) {
            logisticsController.cancelDelivery(order.getId());
        }

        if (!OrderStatus.CANCELED.equals(status)) {
            order.getStatusHistory().add(new StatusHistory(status, processAt));

            orderGateway.update(order);
        } else if (OrderCancellationType.PAYMENT_FAILURE.equals(motive)) {
            order.getPayment().setStatus(PaymentStatus.UNAUTHORIZED);

            orderGateway.update(order);
        }
    }
}
