package com.order.management.system.orderingmicroservice.interfaceadapters.presenters.dtos;

import com.order.management.system.orderingmicroservice.entities.Item;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {

    @NotBlank
    @Schema(description = "SKU do produto" , example = "85789")
    private String sku;

    @NotBlank
    @Schema(description = "Quantidade de itens" , example = "5")
    private Integer quantity;

    @NotBlank
    @Schema(description = "Valor unitário do item" , example = "8.96")
    private BigDecimal value;

    public ItemDto(Item item) {
        this.sku = item.getSku();
        this.quantity = item.getQuantity();
        this.value = item.getValue();
    }
}
