package com.order.management.system.orderingmicroservice.frameworks.external.messaging.logistics;

import com.fasterxml.jackson.databind.JsonNode;
import com.order.management.system.orderingmicroservice.frameworks.external.interfaces.logistics.LogisticsWeb;
import com.order.management.system.orderingmicroservice.util.exception.ExternalInterfaceException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogisticsConsumeMessage {

    @Autowired
    private LogisticsWeb logisticsWeb;

    @RabbitListener(queues = "logistics.process")
    public void handleLogisticsMessages(JsonNode jsonNode) throws ExternalInterfaceException {
        Integer orderId = jsonNode.get("orderId").asInt();
        boolean isToCancel = jsonNode.get("isToCancel").asBoolean();

        if (isToCancel) {
            logisticsWeb.cancelDelivery(orderId);
        } else {
            logisticsWeb.createDelivery(orderId, jsonNode.get("address").get("cep").asText(), jsonNode.get("items"));
        }
    }
}
