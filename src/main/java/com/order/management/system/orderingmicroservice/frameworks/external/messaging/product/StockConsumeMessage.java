package com.order.management.system.orderingmicroservice.frameworks.external.messaging.product;

import com.order.management.system.orderingmicroservice.frameworks.external.interfaces.product.InventoryWeb;
import com.order.management.system.orderingmicroservice.interfaceadapters.controllers.OrderStatusController;
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
    private OrderStatusController statusController;

    @RabbitListener(queues = "stock.process")
    public void confirm(StockMessage stockMessage) throws ExternalInterfaceException {
        inventoryWeb.confirmReservation(stockMessage);

        statusController.updateStatus(OrderStatus.STOCK_SEPARATION, stockMessage.getOrderId());
    }

    @RabbitListener(queues = "stock.cancelation")
    public void cancel(StockMessage stockMessage) throws ExternalInterfaceException {
        inventoryWeb.cancelReservation(stockMessage);
    }

}
