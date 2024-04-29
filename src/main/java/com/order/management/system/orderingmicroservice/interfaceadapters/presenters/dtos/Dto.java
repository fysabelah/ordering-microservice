package com.order.management.system.orderingmicroservice.interfaceadapters.presenters.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class Dto {

    @NotBlank
    @Schema(description = "Documento do cliente", example = "85369855474")
    private String document;

    @NotBlank
    @Schema(description = "Nome do cliente", example = "Fernanda")
    private String name;

    @Schema(description = "Descontos a serem aplicados", example = "8.95")
    private BigDecimal discounts;

    @NotNull
    private ShipmentDto address;

    @NotNull
    private List<ItemDto> items;

    @NotNull
    private PaymentDto payment;
}
