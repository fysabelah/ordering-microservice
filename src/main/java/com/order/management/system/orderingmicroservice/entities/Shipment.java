package com.order.management.system.orderingmicroservice.entities;

import com.order.management.system.orderingmicroservice.interfaceadapters.presenters.dtos.ShipmentDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity(name = "Shipment")
@Table(name = "shipments")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    @Column
    private String tracking;

    @Column
    private String urlTracking;

    @Column(precision = 15, scale = 4)
    private BigDecimal freight;

    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private String neighborhood;

    @Column(nullable = false)
    private String number;

    private String complement;

    @Column(nullable = false)
    private String cep;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String state;

    public Shipment(ShipmentDto dto) {
        this.freight = dto.getFreight();
        this.street = dto.getStreet();
        this.neighborhood = dto.getNeighborhood();
        this.number = dto.getNumber();
        this.complement = dto.getComplement();
        this.cep = dto.getCep();
        this.city = dto.getCity();
        this.state = dto.getState();
    }
}
