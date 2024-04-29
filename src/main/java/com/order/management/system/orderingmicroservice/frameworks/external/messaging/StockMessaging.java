package com.order.management.system.orderingmicroservice.frameworks.external.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.order.management.system.orderingmicroservice.entities.Item;
import com.order.management.system.orderingmicroservice.interfaceadapters.presenters.ProductsPresenter;
import com.order.management.system.orderingmicroservice.interfaceadapters.presenters.messages.StockMessage;
import jakarta.annotation.Resource;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class StockMessaging extends PublishMessageQueue {

    @Value("${messaging.queue.stock}")
    private String queueDoReservation;

    @Value("${messaging.queue.stock.cancelation}")
    private String queueCancelReservation;

    @Resource
    private ProductsPresenter presenter;

    public void sendMessageProcessStock(List<Item> items, Integer orderId) throws JsonProcessingException {
        Message message = getMessage(items, orderId);

        super.sendMessage(message, queueDoReservation);
    }

    private Message getMessage(List<Item> items, Integer orderId) throws JsonProcessingException {
        StockMessage stockMessag = presenter.convert(items, orderId);

        return new Message(super.objectMapper.writeValueAsString(stockMessag).getBytes(StandardCharsets.UTF_8));
    }

    public void sendMessageCancelReservation(Integer orderId, List<Item> items) throws JsonProcessingException {
        Message message = getMessage(items, orderId);

        super.sendMessage(message, queueCancelReservation);
    }
}
