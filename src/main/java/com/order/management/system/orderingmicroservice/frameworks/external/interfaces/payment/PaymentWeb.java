package com.order.management.system.orderingmicroservice.frameworks.external.interfaces.payment;

import com.order.management.system.orderingmicroservice.interfaceadapters.presenters.messages.PaymentMessage;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class PaymentWeb {

    public Map<String, String> processPayment(PaymentMessage message) {
        return handlePayment(message);
    }

    private Map<String, String> handlePayment(PaymentMessage paymentMessage) {
        String number = paymentMessage.getNumber();
        Map<String, String> operation = new HashMap<>();

        String status = "status";
        String message = "mensagem";
        String code = "code";

        if (number.endsWith("0") || number.endsWith("1") || number.endsWith("4")) {
            operation.put(status, "AUTORIZADO");
        } else {
            operation.put(status, "NAO_AUTORIZADO");
        }

        if (number.endsWith("2")) {
            operation.put(code, "05");
            operation.put(message, "Não autorizada");
        } else if (number.endsWith("3")) {
            operation.put(code, "57");
            operation.put(message, "Cartão expirado");
        } else if (number.endsWith("5")) {
            operation.put(code, "78");
            operation.put(message, "Cartão bloqueado");
        } else if (number.endsWith("6")) {
            operation.put(code, "99");
            operation.put(message, "Time Out");
        } else if (number.endsWith("7")) {
            operation.put(code, "77");
            operation.put(message, "Cartão Cancelado");
        } else if (number.endsWith("8")) {
            operation.put(code, "70");
            operation.put(message, "Problemas com o cartão de crédito");
        } else if (number.endsWith("9")) {
            operation.put(code, "99");
            operation.put(message, "Operation Successful / Time Out");
        }

        return operation;
    }
}
