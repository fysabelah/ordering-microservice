package com.order.management.system.orderingmicroservice.interfaceadapters.presenters;

import com.order.management.system.orderingmicroservice.entities.Client;
import com.order.management.system.orderingmicroservice.entities.Order;
import com.order.management.system.orderingmicroservice.interfaceadapters.presenters.messages.ClientMessage;
import org.springframework.stereotype.Component;

@Component
public class ClientPresenter {

    public Client convert(String document, String name) {
        return new Client(document, name);
    }

    public ClientMessage convert(Order order) {
        ClientMessage message = new ClientMessage();

        message.setOrderId(order.getId());
        message.setDocument(order.getClient().getDocument());
        message.setName(order.getClient().getName());
        message.setStreet(order.getShipment().getStreet());
        message.setBlock(order.getShipment().getBlock());
        message.setNumber(order.getShipment().getNumber());
        message.setComplement(order.getShipment().getComplement());
        message.setCep(order.getShipment().getCep());
        message.setNumberOfAttempts(0);

        return message;
    }
}
