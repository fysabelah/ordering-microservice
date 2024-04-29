package com.order.management.system.orderingmicroservice.interfaceadapters.presenters.messages;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientMessage {

    private Integer orderId;

    private String document;

    private String name;

    private String street;

    private String block;

    private String number;

    private String complement;

    private String cep;

    private Integer numberOfAttempts;
}
