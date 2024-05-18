package com.order.management.system.orderingmicroservice.interfaceadapters.presenters;

import com.order.management.system.orderingmicroservice.entities.Order;
import com.order.management.system.orderingmicroservice.interfaceadapters.presenters.dtos.OrderDto;
import com.order.management.system.orderingmicroservice.interfaceadapters.presenters.dtos.ShipmentDto;
import com.order.management.system.orderingmicroservice.interfaceadapters.presenters.dtos.StatusHistoryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderPresenter {

    @Autowired
    private ProductsPresenter productsPresenter;

    @Autowired
    private PaymentPresenter paymentPresenter;

    public OrderDto convert(Order order) {
        OrderDto dto = new OrderDto();

        dto.setId(order.getId());
        dto.setDocument(order.getClient().getDocument());
        dto.setName(order.getClient().getName());
        dto.setDiscounts(order.getDiscounts());
        dto.setAddress(new ShipmentDto(order.getShipment()));
        dto.setItems(productsPresenter.convertToDto(order.getItems()));

        if (order.getPayment() != null) {
            dto.setPayment(paymentPresenter.convert(order.getPayment()));
        }

        dto.setStatus(order.getStatus());
        dto.setMotive(order.getMotive());
        dto.setTotal(order.getTotal());
        dto.setUrlTracking(order.getShipment().getUrlTracking());

        List<StatusHistoryDto> histories = new ArrayList<>();

        order.getStatusHistory().forEach(history -> histories.add(new StatusHistoryDto(history.getStatus(), history.getDate())));

        dto.setStatusHistory(histories);

        return dto;
    }
}
