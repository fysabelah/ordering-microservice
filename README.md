# Sistema de Gerenciamento de Pedidos - Desafio Tech Challenge - Módulo 4

Este repositório refere-se ao microsserviço de pedidos. No total, o projeto envolve 4 microsserviços, sendo eles:

1. Cliente
2. Produto
3. Pedido
4. Logística

Este microsserviço é responsável pelo processamento dos pedidos, desde a criação até a conclusão. Isto inclui receber os pedidos dos clientes, processar pagamento e coordenar com o microsserviço de logística.

## Tecnologias

* Spring Boot para a estrutura do serviço
* Spring Data JPA para manipulação de dados dos pedidos
* Spring Cloud Stream para comunicação baseada em eventos com outros microsserviços
* PostgreSQL para persistência 
