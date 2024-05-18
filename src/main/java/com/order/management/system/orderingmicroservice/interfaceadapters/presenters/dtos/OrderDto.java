package com.order.management.system.orderingmicroservice.interfaceadapters.presenters.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.order.management.system.orderingmicroservice.util.enums.OrderCancellationType;
import com.order.management.system.orderingmicroservice.util.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class OrderDto extends Dto {

    private Integer id;

    private OrderStatus status;

    private OrderCancellationType motive;

    private BigDecimal total;

    private String urlTracking;

    private List<StatusHistoryDto> statusHistory;
}
