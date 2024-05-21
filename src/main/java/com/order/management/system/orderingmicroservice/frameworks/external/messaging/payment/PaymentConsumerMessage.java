package com.order.management.system.orderingmicroservice.frameworks.external.messaging.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.order.management.system.orderingmicroservice.entities.Order;
import com.order.management.system.orderingmicroservice.frameworks.external.interfaces.payment.PaymentWeb;
import com.order.management.system.orderingmicroservice.frameworks.external.messaging.status.StatusPublishMessage;
import com.order.management.system.orderingmicroservice.interfaceadapters.gateways.OrderGateway;
import com.order.management.system.orderingmicroservice.interfaceadapters.presenters.messages.PaymentMessage;
import com.order.management.system.orderingmicroservice.util.enums.OrderCancellationType;
import com.order.management.system.orderingmicroservice.util.enums.OrderStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class PaymentConsumerMessage {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentConsumerMessage.class);

    @Autowired
    private PaymentWeb paymentWeb;

    @Autowired
    private PaymentPublishMessaging paymentPublishMessaging;

    @Autowired
    private StatusPublishMessage statusPublishMessage;

    @Autowired
    private OrderGateway orderGateway;

    @RabbitListener(queues = "order.payment.process")
    public void process(PaymentMessage paymentMessage) throws JsonProcessingException {
        try {
            Order order = orderGateway.findById(paymentMessage.getOrderId());

            if (!OrderStatus.CANCELED.equals(order.getStatus())) {
                Map<String, String> response = paymentWeb.processPayment(paymentMessage);

                if (response.get("status").compareTo("AUTORIZADO") == 0) {
                    statusPublishMessage.sendMessage(order.getId(), OrderStatus.PAYMENT_ACCEPT);
                } else {
                    statusPublishMessage.sendMessage(order.getId(), OrderStatus.CANCELED, OrderCancellationType.PAYMENT_FAILURE);
                }
            }
        } catch (NoSuchElementException e) {
            statusPublishMessage.sendMessage(paymentMessage.getOrderId(), OrderStatus.CANCELED, OrderCancellationType.PAYMENT_FAILURE);
            LOGGER.error(paymentMessage.getOrderId().toString().concat(": ").concat(e.getMessage()));
        }
    }
}
