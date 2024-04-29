package com.order.management.system.orderingmicroservice.frameworks.external.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class PublishMessageQueue {

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private RabbitTemplate template;

    public void sendMessage(Message message, String queue) {
        template.send(queue, message);
    }
}
