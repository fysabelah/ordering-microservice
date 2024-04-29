package com.order.management.system.orderingmicroservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity(name = "Item")
@Table(name = "items")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    @Column(nullable = false)
    private String sku;

    @Column(nullable = false)
    private Integer quantity;

    @Column(precision = 15, scale = 4, nullable = false)
    private BigDecimal value;

    public Item(String sku, Integer quantity, BigDecimal value) {
        this.sku = sku;
        this.quantity = quantity;
        this.value = value;
    }
}
