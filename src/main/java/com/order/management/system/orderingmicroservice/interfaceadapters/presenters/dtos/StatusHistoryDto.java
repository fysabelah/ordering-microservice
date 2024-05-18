package com.order.management.system.orderingmicroservice.interfaceadapters.presenters.dtos;

import com.order.management.system.orderingmicroservice.util.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class StatusHistoryDto {

    private OrderStatus status;

    private LocalDateTime updatedAt;
}
