package com.order.management.system.orderingmicroservice.frameworks.external.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.order.management.system.orderingmicroservice.entities.Client;
import com.order.management.system.orderingmicroservice.entities.Order;
import com.order.management.system.orderingmicroservice.frameworks.external.interfaces.client.ClientWeb;
import com.order.management.system.orderingmicroservice.interfaceadapters.controllers.OrderStatusController;
import com.order.management.system.orderingmicroservice.interfaceadapters.presenters.ClientPresenter;
import com.order.management.system.orderingmicroservice.interfaceadapters.presenters.messages.ClientMessage;
import com.order.management.system.orderingmicroservice.util.MessageUtil;
import com.order.management.system.orderingmicroservice.util.enums.OrderCancellationType;
import com.order.management.system.orderingmicroservice.util.enums.OrderStatus;
import com.order.management.system.orderingmicroservice.util.exception.ExternalInterfaceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Service
public class ClientMessaging extends PublishMessageQueue {

    @Value("${messaging.queue.client}")
    private String queue;

    @Autowired
    private ClientPresenter presenter;

    @Autowired
    private ClientWeb clientWeb;

    @Autowired
    private OrderStatusController orderStatusController;

    private static final int MAX_RETRY_BEFORE_CANCELED = 5;

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientMessaging.class);

    public void sendMessage(Order order) throws JsonProcessingException {
        ClientMessage clientMessage = presenter.convert(order);

        Message message = new Message(super.objectMapper.writeValueAsString(clientMessage).getBytes(StandardCharsets.UTF_8));

        super.sendMessage(message, queue);
    }

    @RabbitListener(queues = "client.validate")
    public void processMessage(Message message) throws JsonProcessingException {
        try {
            ClientMessage client = objectMapper.readValue(message.getBody(), ClientMessage.class);

            Optional<Client> optional = clientWeb.findClientByDocument(client.getDocument());

            if (optional.isEmpty()) {
                clientWeb.insert(client);
            }

            orderStatusController.updateStatus(OrderStatus.PROCESSING, client.getOrderId());
        } catch (IOException e) {
            LOGGER.error(MessageUtil.getMessage("LOG_ERROR_CONVERSION_JSON"), e);

            String messageBody = new String(message.getBody());
            JsonNode node = objectMapper.readTree(messageBody);

            orderStatusController.cancel(node.get("orderId").asInt(), OrderCancellationType.PAYLOAD_ERROR);
        } catch (ExternalInterfaceException e) {
            LOGGER.error(e.getMessage(), e);

            String messageBody = new String(message.getBody());
            JsonNode node = objectMapper.readTree(messageBody);

            int tentativas = node.get("numberOfAttempts").asInt();

            if (tentativas == MAX_RETRY_BEFORE_CANCELED) {
                orderStatusController.cancel(node.get("orderId").asInt(), OrderCancellationType.EXCEEDED_ATTEMPTS);
            }

            ((ObjectNode) node).put("numberOfAttempts", tentativas + 1);

            super.sendMessage(objectMapper.readValue(node.toString(), Message.class), queue);
        }
    }
}
