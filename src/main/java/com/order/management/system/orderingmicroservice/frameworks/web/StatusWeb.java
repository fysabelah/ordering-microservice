package com.order.management.system.orderingmicroservice.frameworks.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.order.management.system.orderingmicroservice.interfaceadapters.controllers.OrderStatusController;
import com.order.management.system.orderingmicroservice.util.enums.OrderCancellationType;
import com.order.management.system.orderingmicroservice.util.enums.OrderStatus;
import com.order.management.system.orderingmicroservice.util.exception.BusinessException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/order/status")
@Tag(name = "Status", description = "Metódos para atualização de status de pedido")
public class StatusWeb {

    @Autowired
    private OrderStatusController controller;

    @PutMapping
    @Operation(summary = "Atualizar status para cancelado")
    public ResponseEntity<Void> cancel(@Parameter(description = "Identificação do pedido", example = "10") @RequestParam Integer orderId) throws JsonProcessingException, BusinessException {
        controller.updateStatus(orderId, OrderStatus.CANCELED, OrderCancellationType.CANCELED_BY_USER, null);

        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/ON_CARRIAGE/{id}")
    @Operation(summary = "Atualizar pedido para o status ON_CARRIAGE")
    public ResponseEntity<Void> update(@Parameter(description = "Identificação do pedido", example = "10") @PathVariable Integer id,
                                                  @Parameter(description = "Código de rastreio", example = "ERIEIR3434") @RequestParam String trackingNumber,
                                                  @Parameter(description = "URL tracking", example = "http://rastreamento.com/ERIEIR3434") @RequestParam String urlTracking) throws BusinessException {
        controller.updateToOnCarrier(id, trackingNumber, urlTracking);

        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/DELIVERED/{id}")
    @Operation(summary = "Atualizar pedido para o status DELIVERED")
    public ResponseEntity<Void> updateToOnCarrier(@Parameter(description = "Identificação do pedido", example = "10") @PathVariable Integer id) throws BusinessException {
        controller.updateToDelivered(id);

        return ResponseEntity.noContent().build();
    }
}
