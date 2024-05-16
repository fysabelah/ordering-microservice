package com.order.management.system.orderingmicroservice.frameworks.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.order.management.system.orderingmicroservice.interfaceadapters.controllers.OrderController;
import com.order.management.system.orderingmicroservice.interfaceadapters.presenters.dtos.Dto;
import com.order.management.system.orderingmicroservice.interfaceadapters.presenters.dtos.OrderDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/order")
@Tag(name = "Pedidos", description = "Metódos para criação de pedidos")
public class OrderWeb {

    @Resource
    private OrderController controller;

    @PostMapping(consumes = "application/json", produces = "application/json")
    @Operation(summary = "Criar pedido")
    public ResponseEntity<OrderDto> insert(@Valid @RequestBody Dto order) throws JsonProcessingException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(controller.insert(order));
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    @Operation(summary = "Recuperar pedido por identificador")
    public ResponseEntity<OrderDto> findById(@Parameter(description = "Identificação do pedido", example = "10") @PathVariable Integer id) {
        return ResponseEntity.ok(controller.findById(id));
    }
}
