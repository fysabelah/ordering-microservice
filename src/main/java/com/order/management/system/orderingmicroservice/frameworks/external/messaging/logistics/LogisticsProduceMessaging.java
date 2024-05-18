package com.order.management.system.orderingmicroservice.frameworks.external.messaging.logistics;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.order.management.system.orderingmicroservice.entities.Item;
import com.order.management.system.orderingmicroservice.entities.Shipment;
import com.order.management.system.orderingmicroservice.frameworks.external.messaging.PublishMessageQueue;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class LogisticsProduceMessaging extends PublishMessageQueue {

    @Value("${messaging.queue.transportation}")
    private String logisticsQueue;

    public void sendMessageCancelDelivery(Integer orderId, List<Item> items, Shipment shipment) throws JsonProcessingException {
        Message message = createMessage(orderId, items, shipment, true);

        super.sendMessage(message, logisticsQueue);
    }

    private Message createMessage(Integer orderId, List<Item> items, Shipment shipment, boolean isToCancel) throws JsonProcessingException {
        ObjectNode json = super.objectMapper.createObjectNode();
        json.put("orderId", orderId);
        json.put("isToCancel", isToCancel);

        String itemsJson = super.objectMapper.writeValueAsString(items);
        json.set("items", super.objectMapper.readValue(itemsJson, JsonNode.class));

        String address = super.objectMapper.writeValueAsString(shipment);
        json.set("address", super.objectMapper.readValue(address, JsonNode.class));

        return new Message(super.objectMapper.writeValueAsString(json).getBytes(StandardCharsets.UTF_8));
    }

    public void sendToCarrier(Integer orderId, List<Item> items, Shipment shipment) throws JsonProcessingException {
        Message message = createMessage(orderId, items, shipment, false);

        super.sendMessage(message, logisticsQueue);
    }
}
