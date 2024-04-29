package com.order.management.system.orderingmicroservice.frameworks.db;

import com.order.management.system.orderingmicroservice.entities.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Integer> {

}
