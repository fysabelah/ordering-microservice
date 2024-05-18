package com.order.management.system.orderingmicroservice.usercase;

import com.order.management.system.orderingmicroservice.entities.Order;
import com.order.management.system.orderingmicroservice.entities.StatusHistory;
import com.order.management.system.orderingmicroservice.util.MessageUtil;
import com.order.management.system.orderingmicroservice.util.enums.OrderCancellationType;
import com.order.management.system.orderingmicroservice.util.enums.OrderStatus;
import com.order.management.system.orderingmicroservice.util.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class OrderStatusUserCase {

    @Autowired
    private Clock clock;

    public boolean hasStock(List<StatusHistory> statusHistory) {
        return statusHistory != null && statusHistory.stream()
                .anyMatch(status -> OrderStatus.STOCK_SEPARATION.compareTo(status.getStatus()) == 0);
    }

    public boolean hasTransportantionHistory(List<StatusHistory> statusHistory) {
        return statusHistory != null && statusHistory.stream()
                .anyMatch(status -> OrderStatus.SHIPPED.equals(status.getStatus()) || OrderStatus.ON_CARRIAGE.equals(status.getStatus()));
    }

    public boolean hasTransportantionHistory(OrderStatus status) {
        return List.of(OrderStatus.SHIPPED, OrderStatus.ON_CARRIAGE).contains(status);
    }

    public void updateStatusCanceled(Order order, OrderCancellationType cancellationType, LocalDateTime processAt) throws BusinessException {
        if (OrderStatus.DELIVERED.equals(order.getStatus())) {
            throw new BusinessException(MessageUtil.getMessage("MESSAGE_STATUS_DELIVERED_TO_CANCEL"));
        }

        order.setStatus(OrderStatus.CANCELED);
        order.setMotive(cancellationType);

        StatusHistory statusHistory = new StatusHistory(OrderStatus.CANCELED, processAt == null ? LocalDateTime.now(clock) : processAt);
        order.getStatusHistory().add(statusHistory);
    }

    public void updateToOnCarrier(Order order, String trackingNumber, String urlTracking) throws BusinessException {
        if (trackingNumber == null || trackingNumber.trim().isEmpty()) {
            throw new IllegalArgumentException(MessageUtil.getMessage("MESSAGE_STATUS_UPDATE_TRACKING_REQUIRED"));
        }

        if (!OrderStatus.SHIPPED.equals(order.getStatus())) {
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
        validTransitions.put(OrderStatus.PAYMENT_ACCEPT, new HashSet<>(Collections.singletonList(OrderStatus.SHIPPING_READY)));
        validTransitions.put(OrderStatus.SHIPPING_READY, new HashSet<>(Collections.singletonList(OrderStatus.SHIPPED)));

        if (validTransitions.containsKey(currentStatus) && validTransitions.get(currentStatus).contains(newStatus)) {
            order.setStatus(newStatus);

            StatusHistory statusHistory = new StatusHistory(newStatus, LocalDateTime.now(clock));
            order.getStatusHistory().add(statusHistory);

            return true;
        }

        return false;
    }

    private boolean hasValidCustomer(List<StatusHistory> statusHistory) {
        return statusHistory != null && statusHistory.stream()
                .anyMatch(status -> OrderStatus.PROCESSING.compareTo(status.getStatus()) == 0);
    }

    public boolean moveToWaitingPayment(List<StatusHistory> statusHistory, OrderStatus status) {
        return hasValidCustomer(statusHistory) && hasStock(statusHistory) && !OrderStatus.WAITING_PAYMENT.equals(status);
    }

    public void updateToDelivered(Order order) throws BusinessException {
        if (!OrderStatus.ON_CARRIAGE.equals(order.getStatus())) {
            throw new BusinessException("MESSAGE_STATUS_UPDATE_INVALID", order.getStatus().toString(), OrderStatus.ON_CARRIAGE.toString());
        }

        order.setStatus(OrderStatus.DELIVERED);

        order.getStatusHistory().add(new StatusHistory(OrderStatus.DELIVERED, LocalDateTime.now(clock)));
    }
}
