package com.order.management.system.orderingmicroservice.frameworks.db;

import com.order.management.system.orderingmicroservice.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    Optional<Order> findByIdAndClientDocument(Integer orderId, String clientDocument);

    @Query("SELECT o FROM Order o JOIN FETCH o.statusHistory WHERE o.id = ?1")
    Optional<Order> findByIdWithStatusHistory(Integer orderId);
}
