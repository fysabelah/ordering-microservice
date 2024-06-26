package com.order.management.system.orderingmicroservice.frameworks.external.messaging.inventory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.order.management.system.orderingmicroservice.frameworks.external.interfaces.product.InventoryWeb;
import com.order.management.system.orderingmicroservice.frameworks.external.messaging.status.StatusPublishMessage;
import com.order.management.system.orderingmicroservice.interfaceadapters.presenters.messages.StockMessage;
import com.order.management.system.orderingmicroservice.util.enums.OrderStatus;
import com.order.management.system.orderingmicroservice.util.exception.ExternalInterfaceException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StockConsumeMessage {

    @Autowired
    private InventoryWeb inventoryWeb;

    @Autowired
    private StatusPublishMessage statusPublishMessage;

    @RabbitListener(queues = "order.stock.process")
    public void confirm(StockMessage stockMessage) throws ExternalInterfaceException, JsonProcessingException {
        inventoryWeb.confirmReservation(stockMessage);

        statusPublishMessage.sendMessage(stockMessage.getOrderId(), OrderStatus.STOCK_SEPARATION);
    }

    @RabbitListener(queues = "order.stock.cancelation")
    public void cancel(StockMessage stockMessage) throws ExternalInterfaceException {
        inventoryWeb.cancelReservation(stockMessage);
    }

}
