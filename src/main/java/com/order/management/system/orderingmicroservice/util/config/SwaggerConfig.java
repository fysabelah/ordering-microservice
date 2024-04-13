package com.order.management.system.orderingmicroservice.util.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI apiDocConfig() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("Sistema de Gerenciamento de Pedidos")
                                .description("API para Microsserviço de Pedido")
                                .version("1.0.0")
                );
    }
}
