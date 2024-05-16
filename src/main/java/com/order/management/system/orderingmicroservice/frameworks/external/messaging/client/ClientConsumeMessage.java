package com.order.management.system.orderingmicroservice.frameworks.external.messaging.client;

import com.order.management.system.orderingmicroservice.entities.Client;
import com.order.management.system.orderingmicroservice.frameworks.external.interfaces.client.ClientWeb;
import com.order.management.system.orderingmicroservice.interfaceadapters.controllers.OrderStatusController;
import com.order.management.system.orderingmicroservice.interfaceadapters.presenters.messages.ClientMessage;
import com.order.management.system.orderingmicroservice.util.enums.OrderStatus;
import com.order.management.system.orderingmicroservice.util.exception.ExternalInterfaceException;
import com.order.management.system.orderingmicroservice.util.exception.ProcessMessageException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientConsumeMessage {

    @Autowired
    private OrderStatusController orderStatusController;

    @Autowired
    private ClientWeb clientWeb;

    @RabbitListener(queues = "client.validate")
    public void processMessage(ClientMessage client) {
        try {
            Optional<Client> optional = clientWeb.findClientByDocument(client.getDocument());

            if (optional.isEmpty()) {
                clientWeb.insert(client);
            }

            orderStatusController.updateStatus(OrderStatus.PROCESSING, client.getOrderId());
        } catch (ExternalInterfaceException e) {
            throw new ProcessMessageException(e);
        }
    }
}
