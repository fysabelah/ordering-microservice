package com.order.management.system.orderingmicroservice.frameworks.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.order.management.system.orderingmicroservice.interfaceadapters.controllers.OrderStatusController;
import com.order.management.system.orderingmicroservice.util.enums.OrderCancellationType;
import com.order.management.system.orderingmicroservice.util.exception.BusinessException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/v1/status")
@Tag(name = "Status", description = "Metódos para atualização de status de pedido")
public class StatusWeb {

    @Autowired
    private OrderStatusController controller;

    @PutMapping
    @Operation(summary = "Atualizar status para cancelado")
    public ResponseEntity<Void> cancel(@Parameter(description = "Identificação do pedido", example = "10") @RequestParam Integer orderId) throws JsonProcessingException {
        controller.cancel(orderId, OrderCancellationType.CANCELED_BY_USER);

        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{id}")
    @Operation(summary = "Atualizar pedido para o status ON_CARRIAGE")
    public ResponseEntity<Void> updateToOnCarrier(@Parameter(description = "Identificação do pedido", example = "10") @PathVariable Integer id,
                                                  @Parameter(description = "Código de rastreio", example = "ERIEIR3434") @RequestParam String trackingNumber,
                                                  @Parameter(description = "URL tracking", example = "http://rastreamento.com/ERIEIR3434") @RequestParam String urlTracking) throws BusinessException {
        controller.updateStatus(id, trackingNumber, urlTracking);

        return ResponseEntity.noContent().build();
    }
}
