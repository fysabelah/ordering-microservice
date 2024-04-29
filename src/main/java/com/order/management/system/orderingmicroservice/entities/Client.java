package com.order.management.system.orderingmicroservice.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "Client")
@Table(name = "clients")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class Client {

    @Id
    private String document;

    @Column(nullable = false)
    private String name;
}
