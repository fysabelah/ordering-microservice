package com.order.management.system.orderingmicroservice.frameworks.external.messaging.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.order.management.system.orderingmicroservice.frameworks.external.messaging.PublishMessageQueue;
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
        ObjectNode statusMessage = super.objectMapper.createObjectNode();
        statusMessage.put("orderId", orderId.toString());
        statusMessage.put("status", status.name());
        statusMessage.put("motive", cancellationType.name());

        Message payload = new Message(super.objectMapper.writeValueAsString(statusMessage).getBytes(StandardCharsets.UTF_8));

        super.sendMessage(payload, statusUpdateQueue);
    }

    public void sendMessage(Integer orderId, OrderStatus status) throws JsonProcessingException {
        ObjectNode statusMessage = super.objectMapper.createObjectNode();
        statusMessage.put("orderId", orderId.toString());
        statusMessage.put("status", status.name());
        statusMessage.put("motive", "");

        Message payload = new Message(super.objectMapper.writeValueAsString(statusMessage).getBytes(StandardCharsets.UTF_8));

        super.sendMessage(payload, statusUpdateQueue);
    }
}
