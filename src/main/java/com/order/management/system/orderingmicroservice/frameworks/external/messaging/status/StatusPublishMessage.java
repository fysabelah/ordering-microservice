package com.order.management.system.orderingmicroservice.frameworks.external.messaging.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.order.management.system.orderingmicroservice.frameworks.external.messaging.PublishMessageQueue;
import com.order.management.system.orderingmicroservice.interfaceadapters.presenters.messages.StatusMessage;
import com.order.management.system.orderingmicroservice.util.enums.OrderCancellationType;
import com.order.management.system.orderingmicroservice.util.enums.OrderStatus;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class StatusPublishMessage extends PublishMessageQueue {

    @Value("${messaging.queue.status}")
    private String statusUpdateQueue;

    public void sendMessage(Integer orderId, OrderStatus status, OrderCancellationType cancellationType) throws JsonProcessingException {
        super.sendMessage(create(orderId, status, cancellationType), statusUpdateQueue);
    }

    private Message create(Integer orderId, OrderStatus status, OrderCancellationType cancellationType) throws JsonProcessingException {
        StatusMessage statusMessage = new StatusMessage(orderId, status, cancellationType);

        return new Message(super.objectMapper.writeValueAsString(statusMessage).getBytes(StandardCharsets.UTF_8));
    }

    public void sendMessage(Integer orderId, OrderStatus status) throws JsonProcessingException {
        super.sendMessage(create(orderId, status, null), statusUpdateQueue);
    }
}
