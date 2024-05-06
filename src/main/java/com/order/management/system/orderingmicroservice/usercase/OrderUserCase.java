package com.order.management.system.orderingmicroservice.usercase;

import com.order.management.system.orderingmicroservice.entities.Item;
import com.order.management.system.orderingmicroservice.entities.Order;
import com.order.management.system.orderingmicroservice.entities.Shipment;
import com.order.management.system.orderingmicroservice.entities.StatusHistory;
import com.order.management.system.orderingmicroservice.interfaceadapters.presenters.ClientPresenter;
import com.order.management.system.orderingmicroservice.interfaceadapters.presenters.ProductsPresenter;
import com.order.management.system.orderingmicroservice.interfaceadapters.presenters.dtos.Dto;
import com.order.management.system.orderingmicroservice.util.MessageUtil;
import com.order.management.system.orderingmicroservice.util.enums.OrderCancellationType;
import com.order.management.system.orderingmicroservice.util.enums.OrderStatus;
import com.order.management.system.orderingmicroservice.util.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class OrderUserCase {

    @Autowired
    private Clock clock;

    @Autowired
    private ClientPresenter clientPresenter;

    @Autowired
    private ProductsPresenter productsPresenter;

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderUserCase.class);

    public void updateToCancel(Order order, OrderCancellationType motive) {
        order.setStatus(OrderStatus.CANCELED);
        order.setMotive(motive);

        StatusHistory statusHistory = new StatusHistory(OrderStatus.CANCELED, LocalDateTime.now(clock));
        order.getStatusHistory().add(statusHistory);
    }

    public void updateToOnCarrier(Order order, String trackingNumber) throws BusinessException {
        if (trackingNumber == null || trackingNumber.trim().isEmpty()) {
            throw new IllegalArgumentException(MessageUtil.getMessage("MESSAGE_STATUS_UPDATE_TRACKING_REQUIRED"));
        }

        if (OrderStatus.SHIPPING_READY.compareTo(order.getStatus()) != 0) {
            LOGGER.warn(MessageUtil.getMessage("LOG_MESSAGE_ORDER_CANT_BE_UPDATED", order.getId().toString()));
            throw new BusinessException("MESSAGE_STATUS_UPDATE_INVALID", order.getStatus().toString(), OrderStatus.ON_CARRIAGE.toString());
        }

        order.setStatus(OrderStatus.ON_CARRIAGE);

        StatusHistory statusHistory = new StatusHistory(OrderStatus.ON_CARRIAGE, LocalDateTime.now(clock));
        order.getStatusHistory().add(statusHistory);
    }

    public Order createOrder(Dto dto) {
        Order order = new Order();

        order.setClient(clientPresenter.convert(dto));
        order.setStatus(OrderStatus.PENDING);

        order.setItems(productsPresenter.convertToEntity(dto.getItems()));
        order.setShipment(new Shipment(dto.getAddress()));
        order.setDiscounts(dto.getDiscounts() == null ? BigDecimal.ZERO : dto.getDiscounts());

        StatusHistory statusHistory = new StatusHistory(OrderStatus.PENDING, LocalDateTime.now(clock));
        order.setStatusHistory(List.of(statusHistory));

        BigDecimal total = BigDecimal.ZERO;

        for (Item item : order.getItems()) {
            total = total.add(item.getValue().multiply(new BigDecimal(item.getQuantity())));
        }

        total = total.add(order.getShipment().getFreight())
                .subtract(order.getDiscounts());

        order.setTotal(total);

        return order;
    }
}
