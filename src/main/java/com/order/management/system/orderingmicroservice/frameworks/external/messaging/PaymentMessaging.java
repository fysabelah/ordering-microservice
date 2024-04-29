package com.order.management.system.orderingmicroservice.frameworks.external.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.order.management.system.orderingmicroservice.interfaceadapters.presenters.PaymentPresenter;
import com.order.management.system.orderingmicroservice.interfaceadapters.presenters.dtos.PaymentDto;
import com.order.management.system.orderingmicroservice.interfaceadapters.presenters.messages.PaymentMessage;
import jakarta.annotation.Resource;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class PaymentMessaging extends PublishMessageQueue {

    @Value("${messaging.queue.payment}")
    private String queue;


    @Resource
    private PaymentPresenter presenter;

    public void sendMessage(PaymentDto payment, Integer orderId) throws JsonProcessingException {
        PaymentMessage paymentMessage = presenter.convert(payment, orderId);

        Message message = new Message(super.objectMapper.writeValueAsString(paymentMessage).getBytes(StandardCharsets.UTF_8));

        super.sendMessage(message, queue);
    }
}
