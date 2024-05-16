package com.order.management.system.orderingmicroservice.interfaceadapters.presenters.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class Dto {

    @NotBlank
    @Length(min = 8, max = 14)
    @Schema(description = "Documento do cliente sem pontuação", example = "85369855474")
    private String document;

    @NotBlank
    @Schema(description = "Nome do cliente", example = "Fernanda")
    private String name;

    @NotBlank
    @Email
    @Schema(description = "E-mail do cliente", example = "nanda@gmail.com")
    private String email;

    @NotBlank
    @Schema(description = "Telefone do cliente")
    @Pattern(regexp = "\\+[0-9]{2} [0-9]{2} 9 [0-9]{4}-[0-9]{4}")
    private String cellphone;

    @Schema(description = "Descontos a serem aplicados", example = "8.95")
    private BigDecimal discounts;

    @NotNull
    private ShipmentDto address;

    @NotNull
    private List<ItemDto> items;

    @NotNull
    private PaymentDto payment;
}
