package com.order.management.system.orderingmicroservice.entities;

import com.order.management.system.orderingmicroservice.util.enums.PaymentStatus;
import com.order.management.system.orderingmicroservice.util.enums.PaymentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity(name = "Payment")
@Table(name = "payments")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    @Column
    private String brand;

    @Column(precision = 15, scale = 4)
    private BigDecimal total;

    @Enumerated(value = EnumType.STRING)
    private PaymentType type;

    @Column
    private Integer installments;

    @Column
    private String number;

    @Column
    private String holder;

    @Column
    private String expirationDate;

    @Column
    private String securityCode;

    @Enumerated(value = EnumType.STRING)
    private PaymentStatus status;

}