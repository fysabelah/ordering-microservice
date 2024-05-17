package com.order.management.system.orderingmicroservice.interfaceadapters.presenters.messages;

import com.order.management.system.orderingmicroservice.util.enums.OrderCancellationType;
import com.order.management.system.orderingmicroservice.util.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class StatusMessage {

    private Integer orderId;

    private OrderStatus status;

    private OrderCancellationType motive;

    private LocalDateTime processAt;
}
