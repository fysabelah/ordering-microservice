package com.order.management.system.orderingmicroservice.interfaceadapters.presenters.messages;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientMessage {

    private Integer orderId;

    private String document;

    private String name;

    private String email;

    private String cellphone;

    private String street;

    private String neighborhood;

    private String number;

    private String state;

    private String city;

    private String complement;

    private String cep;

    private Integer numberOfAttempts;
}
