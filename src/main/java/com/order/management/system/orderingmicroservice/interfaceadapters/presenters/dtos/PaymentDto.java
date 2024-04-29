package com.order.management.system.orderingmicroservice.interfaceadapters.presenters.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.order.management.system.orderingmicroservice.util.enums.PaymentType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(value = {"total"}, allowGetters = true)
public class PaymentDto {

    @NotBlank
    @Schema(description = "Bandeira do cartão", example = "Visa")
    private String brand;

    private String total;

    @Schema(description = "Tipo de pagamento")
    private PaymentType type;

    @PositiveOrZero
    @Schema(description = "Quantidade de parcelas. Permitido apenas para opção CREDIT", example = "2")
    private Integer installments;

    @NotBlank
    @Schema(description = "Número do cartão", example = "4532117080573703")
    private String number;

    @NotBlank
    @Schema(description = "Titular do cartão", example = "Junin Silva")
    private String holder;

    @NotBlank
    @Schema(description = "Data de expiração do cartão", example = "12/2019")
    @Pattern(regexp = "[0-9]{2}/[0-9]{2}")
    private String expirationDate;

    @NotBlank
    @Schema(description = "Código de segurança", example = "023")
    @Pattern(regexp = "[0-9]{3}")
    private String securityCode;
}
