package com.order.management.system.orderingmicroservice.interfaceadapters.presenters.dtos;

import com.order.management.system.orderingmicroservice.entities.Shipment;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShipmentDto {

    @NotBlank
    @Schema(description = "Rua/Avenida", example = "Rua 34")
    private String street;

    @Schema(description = "Quadra", example = "qd 735")
    private String neighborhood;

    @Schema(description = "Cidade", example = "São Paulo")
    private String city;

    @Schema(description = "Estado", example = "São Paulo")
    private String state;

    @NotBlank
    @Schema(description = "Número do lote", example = "95")
    private String number;

    @Schema(description = "Orientações/Pontos de referência", example = "Próximo ao mercado Quintada")
    private String complement;

    @NotBlank
    @Pattern(regexp = "[0-9]{5}-[0-9]{3}")
    private String cep;

    @PositiveOrZero
    @Schema(description = "Valor do frete", example = "10.98")
    private BigDecimal freight;

    public ShipmentDto(Shipment shipment) {
        this.street = shipment.getStreet();
        this.neighborhood = shipment.getNeighborhood();
        this.number = shipment.getNumber();
        this.city = shipment.getCity();
        this.state = shipment.getState();
        this.complement = shipment.getComplement();
        this.cep = shipment.getCep();
        this.freight = shipment.getFreight();
    }
}
