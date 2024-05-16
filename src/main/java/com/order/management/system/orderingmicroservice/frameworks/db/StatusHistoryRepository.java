package com.order.management.system.orderingmicroservice.frameworks.db;

import com.order.management.system.orderingmicroservice.entities.StatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusHistoryRepository extends JpaRepository<StatusHistory, Integer> {

}
