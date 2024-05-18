package com.order.management.system.orderingmicroservice.frameworks.db;

import com.order.management.system.orderingmicroservice.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Query("SELECT o FROM Order o JOIN FETCH o.statusHistory WHERE o.id = ?1")
    Optional<Order> findByIdWithStatusHistory(Integer orderId);

    @Query("SELECT o FROM Order o JOIN FETCH o.items WHERE o.id = :order")
    Optional<Order> findByWithItems(@Param(value = "order") Integer orderId);
}
