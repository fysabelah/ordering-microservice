package com.order.management.system.orderingmicroservice.frameworks.external.messaging.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.order.management.system.orderingmicroservice.entities.Order;
import com.order.management.system.orderingmicroservice.frameworks.external.messaging.PublishMessageQueue;
import com.order.management.system.orderingmicroservice.interfaceadapters.presenters.ClientPresenter;
import com.order.management.system.orderingmicroservice.interfaceadapters.presenters.messages.ClientMessage;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class ClientPublishMessage extends PublishMessageQueue {

    @Value("${messaging.queue.client}")
    private String queue;

    @Autowired
    private ClientPresenter presenter;

    public void sendMessage(Order order) throws JsonProcessingException {
        ClientMessage clientMessage = presenter.convert(order);

        Message message = new Message(super.objectMapper.writeValueAsString(clientMessage).getBytes(StandardCharsets.UTF_8));

        super.sendMessage(message, queue);
    }
}
