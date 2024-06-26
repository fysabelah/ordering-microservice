package com.order.management.system.orderingmicroservice.interfaceadapters.presenters.messages;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class StockMessage {

    private Integer orderId;

    private List<Product> products;

    @Getter
    @Setter
    public static class Product {

        private String sku;

        private Integer quantity;

        private BigDecimal value;

        private String reservation;

        public Product(String sku, Integer quantity, BigDecimal value, String reservation) {
            this.sku = sku;
            this.quantity = quantity;
            this.value = value;
            this.reservation = reservation;
        }
    }
}
