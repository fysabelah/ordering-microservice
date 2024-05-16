package com.order.management.system.orderingmicroservice.entities;

import com.order.management.system.orderingmicroservice.util.enums.OrderCancellationType;
import com.order.management.system.orderingmicroservice.util.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity(name = "Order")
@Table(name = "orders")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private OrderStatus status;

    @Enumerated(value = EnumType.STRING)
    private OrderCancellationType motive;

    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Client client;

    @Column(precision = 15, scale = 4)
    private BigDecimal total;

    @Column(precision = 15, scale = 4)
    private BigDecimal discounts;

    @OneToOne
    private Payment payment;

    @OneToMany(cascade = CascadeType.PERSIST)
    private List<Item> items;

    @OneToOne(cascade = CascadeType.ALL)
    private Shipment shipment;

    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private List<StatusHistory> statusHistory;
}
