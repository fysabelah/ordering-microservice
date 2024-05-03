package com.order.management.system.orderingmicroservice.frameworks.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.order.management.system.orderingmicroservice.interfaceadapters.controllers.OrderController;
import com.order.management.system.orderingmicroservice.interfaceadapters.presenters.dtos.Dto;
import com.order.management.system.orderingmicroservice.interfaceadapters.presenters.dtos.OrderDto;
import com.order.management.system.orderingmicroservice.util.enums.OrderCancellationType;
import com.order.management.system.orderingmicroservice.util.exception.BusinessException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/v1/order")
@Tag(name = "Pedidos", description = "Metódos para criação de pedidos")
public class OrderWeb {

    @Resource
    private OrderController controller;

    @PostMapping
    @Operation(summary = "Criar pedido")
    public ResponseEntity<OrderDto> insert(@Valid @RequestBody Dto order) throws JsonProcessingException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(controller.insert(order));
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "Recuperar pedido por identificador")
    public ResponseEntity<OrderDto> findById(@Parameter(description = "Identificação do pedido", example = "10") @PathVariable Integer id) {
        return ResponseEntity.ok(controller.findById(id));
    }

    @PutMapping
    @Operation(summary = "Cancelar pedido")
    public ResponseEntity<Void> cancel(@Parameter(description = "Identificação do pedido", example = "10") @RequestParam Integer orderId,
                                       @Parameter(description = "Documento do cliente", example = "12588568996") @RequestParam String document) throws JsonProcessingException {
        controller.cancel(orderId, document, OrderCancellationType.CANCELED_BY_USER);

        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{id}")
    @Operation(summary = "Atualizar pedido para o status ON_CARRIER")
    public ResponseEntity<Void> updateToOnCarrier(@Parameter(description = "Identificação do pedido", example = "10") @PathVariable Integer id,
                                                  @Parameter(description = "Código de rastreio", example = "ERIEIR3434") @RequestParam String trackingNumber) throws BusinessException {
        controller.updateToOnCarrier(id, trackingNumber);

        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/port")
    @Operation(summary = "Veja a porta de execução do serviço")
    public String findPort(@Value("${eureka.instance.instance-id}") String port) {
        return String.format("Rodando na porta: %s", port);
    }
}
