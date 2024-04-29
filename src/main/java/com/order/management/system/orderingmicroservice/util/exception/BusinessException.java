package com.order.management.system.orderingmicroservice.util.exception;

import com.order.management.system.orderingmicroservice.util.MessageUtil;

public class BusinessException extends Exception {

    public BusinessException(String code, String... values) {
        super(MessageUtil.getMessage(code, values));
    }
}
