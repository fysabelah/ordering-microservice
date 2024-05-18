package com.order.management.system.orderingmicroservice.frameworks.external.messaging.logistics;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.order.management.system.orderingmicroservice.frameworks.external.messaging.PublishMessageQueue;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class LogisticsMessaging extends PublishMessageQueue {

    @Value("${messaging.queue.transportation}")
    private String logisticsQueue;

    public void sendMessageCancelDelivery(Integer orderId) throws JsonProcessingException {
        ObjectNode json = super.objectMapper.createObjectNode();
        json.put("orderId", orderId);
        json.put("isToCancel", true);

        Message message = new Message(super.objectMapper.writeValueAsString(json).getBytes(StandardCharsets.UTF_8));

        super.sendMessage(message, logisticsQueue);
    }

    public void sendToCarrier(Integer orderId) throws JsonProcessingException {
        ObjectNode json = super.objectMapper.createObjectNode();
        json.put("orderId", orderId);
        json.put("isToCancel", false);

        Message message = new Message(super.objectMapper.writeValueAsString(json).getBytes(StandardCharsets.UTF_8));

        super.sendMessage(message, logisticsQueue);
    }
}
