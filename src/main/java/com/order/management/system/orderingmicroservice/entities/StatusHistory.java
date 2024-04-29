package com.order.management.system.orderingmicroservice.entities;


import com.order.management.system.orderingmicroservice.util.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity(name = "StatusHistory")
@Table(name = "status_history")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class StatusHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private OrderStatus status;

    @Column(name = "date", columnDefinition = "TIMESTAMP")
    private LocalDateTime date;

    public StatusHistory(OrderStatus status, LocalDateTime date) {
        this.status = status;
        this.date = date;
    }
}
