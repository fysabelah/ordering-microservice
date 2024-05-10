package com.order.management.system.orderingmicroservice.usercase;

import com.order.management.system.orderingmicroservice.entities.Order;
import com.order.management.system.orderingmicroservice.entities.StatusHistory;
import com.order.management.system.orderingmicroservice.util.MessageUtil;
import com.order.management.system.orderingmicroservice.util.enums.OrderCancellationType;
import com.order.management.system.orderingmicroservice.util.enums.OrderStatus;
import com.order.management.system.orderingmicroservice.util.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class OrderStatusUserCase {

    @Autowired
    private Clock clock;

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderStatusUserCase.class);

    public boolean hasStock(List<StatusHistory> statusHistory) {
        return statusHistory != null && statusHistory.stream()
                .anyMatch(status -> OrderStatus.STOCK_SEPARATION.compareTo(status.getStatus()) == 0);
    }

    public boolean hasTransportantionHistory(List<StatusHistory> statusHistory) {
        return statusHistory != null && statusHistory.stream()
                .anyMatch(status -> OrderStatus.SHIPPED.compareTo(status.getStatus()) == 0);
    }

    public void updateStatusCanceled(Order order, OrderCancellationType cancellationType) {
        order.setStatus(OrderStatus.CANCELED);
        order.setMotive(cancellationType);

        StatusHistory statusHistory = new StatusHistory(OrderStatus.CANCELED, LocalDateTime.now(clock));
        order.getStatusHistory().add(statusHistory);
    }

    public void updateToOnCarrier(Order order, String trackingNumber, String urlTracking) throws BusinessException {
        if (trackingNumber == null || trackingNumber.trim().isEmpty()) {
            throw new IllegalArgumentException(MessageUtil.getMessage("MESSAGE_STATUS_UPDATE_TRACKING_REQUIRED"));
        }

        if (OrderStatus.SHIPPED.compareTo(order.getStatus()) != 0) {
            LOGGER.warn(MessageUtil.getMessage("LOG_MESSAGE_ORDER_CANT_BE_UPDATED", order.getId().toString()));

            throw new BusinessException("MESSAGE_STATUS_UPDATE_INVALID", order.getStatus().toString(), OrderStatus.ON_CARRIAGE.toString());
        }

        order.setStatus(OrderStatus.ON_CARRIAGE);
        order.getShipment().setTracking(trackingNumber);
        order.getShipment().setUrlTracking(urlTracking);

        StatusHistory statusHistory = new StatusHistory(OrderStatus.ON_CARRIAGE, LocalDateTime.now(clock));
        order.getStatusHistory().add(statusHistory);
    }

    public boolean validateStatusMap(Order order, OrderStatus newStatus) {
        OrderStatus currentStatus = order.getStatus();

        Map<OrderStatus, Set<OrderStatus>> validTransitions = new HashMap<>();
        validTransitions.put(OrderStatus.PENDING, new HashSet<>(Arrays.asList(OrderStatus.PROCESSING, OrderStatus.STOCK_SEPARATION)));
        validTransitions.put(OrderStatus.PROCESSING, new HashSet<>(Arrays.asList(OrderStatus.STOCK_SEPARATION, OrderStatus.WAITING_PAYMENT)));
        validTransitions.put(OrderStatus.WAITING_PAYMENT, new HashSet<>(Collections.singletonList(OrderStatus.PAYMENT_ACCEPT)));
        validTransitions.put(OrderStatus.STOCK_SEPARATION, new HashSet<>(Arrays.asList(OrderStatus.PROCESSING, OrderStatus.WAITING_PAYMENT)));
        validTransitions.put(OrderStatus.SHIPPING_READY, new HashSet<>(Collections.singletonList(OrderStatus.SHIPPED)));
        validTransitions.put(OrderStatus.SHIPPED, new HashSet<>(Collections.singletonList(OrderStatus.DELIVERED)));

        if (validTransitions.containsKey(currentStatus) && validTransitions.get(currentStatus).contains(newStatus)) {
            if ((currentStatus == OrderStatus.PROCESSING && hasStock(order.getStatusHistory())) ||
                    (currentStatus == OrderStatus.STOCK_SEPARATION && hasValidCustomer(order.getStatusHistory()))) {
                newStatus = OrderStatus.WAITING_PAYMENT;
            }

            order.setStatus(newStatus);

            StatusHistory statusHistory = new StatusHistory(OrderStatus.CANCELED, LocalDateTime.now(clock));
            order.getStatusHistory().add(statusHistory);

            return true;
        }

        return false;
    }

    private boolean hasValidCustomer(List<StatusHistory> statusHistory) {
        return statusHistory != null && statusHistory.stream()
                .anyMatch(status -> OrderStatus.PROCESSING.compareTo(status.getStatus()) == 0);
    }

}
