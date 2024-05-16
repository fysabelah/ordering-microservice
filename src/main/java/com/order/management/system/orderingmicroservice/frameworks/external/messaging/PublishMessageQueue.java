package com.order.management.system.orderingmicroservice.frameworks.external.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class PublishMessageQueue {

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private RabbitTemplate template;

    protected void sendMessage(Message message, String queue) {
        message.getMessageProperties().setContentEncoding(StandardCharsets.UTF_8.name());
        message.getMessageProperties().setContentType("application/json");

        template.send(queue, message);
    }
}
