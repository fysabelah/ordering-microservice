package com.order.management.system.orderingmicroservice.frameworks.db;

import com.order.management.system.orderingmicroservice.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    Optional<Order> findByIdAndClientDocument(Integer orderId, String clientDocument);
}
