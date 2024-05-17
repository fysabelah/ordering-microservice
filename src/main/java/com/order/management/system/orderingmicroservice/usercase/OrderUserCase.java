package com.order.management.system.orderingmicroservice.usercase;

import com.order.management.system.orderingmicroservice.entities.Item;
import com.order.management.system.orderingmicroservice.entities.Order;
import com.order.management.system.orderingmicroservice.entities.Shipment;
import com.order.management.system.orderingmicroservice.entities.StatusHistory;
import com.order.management.system.orderingmicroservice.interfaceadapters.presenters.ClientPresenter;
import com.order.management.system.orderingmicroservice.interfaceadapters.presenters.PaymentPresenter;
import com.order.management.system.orderingmicroservice.interfaceadapters.presenters.ProductsPresenter;
import com.order.management.system.orderingmicroservice.interfaceadapters.presenters.dtos.Dto;
import com.order.management.system.orderingmicroservice.util.enums.OrderStatus;
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

    @Autowired
    private PaymentPresenter paymentPresenter;

    public Order createOrder(Dto dto) {
        Order order = new Order();

        order.setClient(clientPresenter.convert(dto));
        order.setStatus(OrderStatus.PENDING);

        order.setItems(productsPresenter.convertToEntity(dto.getItems()));
        order.setShipment(new Shipment(dto.getAddress()));
        order.setDiscounts(dto.getDiscounts() == null ? BigDecimal.ZERO : dto.getDiscounts());

        order.setPayment(paymentPresenter.convert(dto.getPayment()));

        StatusHistory statusHistory = new StatusHistory(OrderStatus.PENDING, LocalDateTime.now(clock));
        order.setStatusHistory(List.of(statusHistory));

        BigDecimal total = BigDecimal.ZERO;

        for (Item item : order.getItems()) {
            total = total.add(item.getValue().multiply(new BigDecimal(item.getQuantity())));
        }

        total = total.add(order.getShipment().getFreight())
                .subtract(order.getDiscounts());

        order.setTotal(total);
        order.getPayment().setTotal(total);

        return order;
    }
}
