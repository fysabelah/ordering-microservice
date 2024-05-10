package com.order.management.system.orderingmicroservice.frameworks.external.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class TransportationMessaging extends PublishMessageQueue {

    @Value("${messaging.queue.transportation.cancel}")
    private String queueCancelTransportation;

    public void sendMessageCancelDelivery(Integer orderId) throws JsonProcessingException {
        ObjectNode json = super.objectMapper.createObjectNode();
        json.put("orderId", orderId);

        Message message = new Message(super.objectMapper.writeValueAsString(json).getBytes(StandardCharsets.UTF_8));

        super.sendMessage(message, queueCancelTransportation);
    }
}
