package com.order.management.system.orderingmicroservice.interfaceadapters.presenters;

import com.order.management.system.orderingmicroservice.entities.Payment;
import com.order.management.system.orderingmicroservice.interfaceadapters.presenters.dtos.PaymentDto;
import com.order.management.system.orderingmicroservice.interfaceadapters.presenters.messages.PaymentMessage;
import com.order.management.system.orderingmicroservice.util.enums.PaymentStatus;
import com.order.management.system.orderingmicroservice.util.enums.PaymentType;
import org.springframework.stereotype.Component;

@Component
public class PaymentPresenter {

    private static final String REPLACE = "*";

    public Payment convert(PaymentDto paymentDto) {
        Payment payment = new Payment();

        payment.setBrand(paymentDto.getBrand());
        payment.setTotal(paymentDto.getTotal());
        payment.setType(paymentDto.getType());
        payment.setInstallments(getInstallments(paymentDto.getType(), paymentDto.getInstallments()));
        payment.setNumber(paymentDto.getNumber());
        payment.setHolder(paymentDto.getHolder());
        payment.setExpirationDate(paymentDto.getExpirationDate());
        payment.setSecurityCode(paymentDto.getSecurityCode());
        payment.setStatus(PaymentStatus.PENDING);

        return payment;
    }

    public PaymentMessage convert(PaymentDto paymentDto, Integer orderId) {
        PaymentMessage message = new PaymentMessage();

        message.setOrderId(orderId);
        message.setBrand(paymentDto.getBrand());
        message.setTotal(paymentDto.getTotal() != null ? paymentDto.getTotal().toString() : null);
        message.setType(paymentDto.getType());
        message.setInstallments(getInstallments(paymentDto.getType(), paymentDto.getInstallments()));
        message.setNumber(paymentDto.getNumber());
        message.setHolder(paymentDto.getHolder());
        message.setExpirationDate(paymentDto.getExpirationDate());
        message.setSecurityCode(paymentDto.getSecurityCode());

        return message;
    }

    public PaymentDto convert(Payment payment) {
        PaymentDto dto = new PaymentDto();

        dto.setBrand(payment.getBrand());
        dto.setTotal(payment.getTotal());
        dto.setType(payment.getType());
        dto.setInstallments(getInstallments(payment.getType(), payment.getInstallments()));
        dto.setNumber(removeSensitiveInfo(payment.getNumber(), 4));
        dto.setHolder(payment.getHolder());
        dto.setExpirationDate(payment.getExpirationDate());
        dto.setSecurityCode(removeSensitiveInfo(payment.getSecurityCode(), 0));

        return dto;
    }

    private static Integer getInstallments(PaymentType paymentType, Integer installments) {
        if (PaymentType.CREDIT.compareTo(paymentType) == 0) {
            return installments;
        }

        return 0;
    }

    private String removeSensitiveInfo(String info, Integer limit) {
        String newNumber = "";

        for (int i = 0; i < info.length() - limit; i++) {
            newNumber = newNumber.concat(REPLACE);
        }

        return newNumber.concat(info.substring(info.length() - limit));
    }
}
