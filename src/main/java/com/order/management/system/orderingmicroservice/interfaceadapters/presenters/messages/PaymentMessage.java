package com.order.management.system.orderingmicroservice.interfaceadapters.presenters.messages;

import com.order.management.system.orderingmicroservice.util.enums.PaymentType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentMessage {

    private Integer orderId;

    private String brand;

    private String total;

    private PaymentType type;

    private Integer installments;

    private String number;

    private String holder;

    private String expirationDate;

    private String securityCode;
}
