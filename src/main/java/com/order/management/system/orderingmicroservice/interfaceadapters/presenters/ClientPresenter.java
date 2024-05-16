package com.order.management.system.orderingmicroservice.interfaceadapters.presenters;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.order.management.system.orderingmicroservice.entities.Client;
import com.order.management.system.orderingmicroservice.entities.Order;
import com.order.management.system.orderingmicroservice.interfaceadapters.presenters.dtos.Dto;
import com.order.management.system.orderingmicroservice.interfaceadapters.presenters.messages.ClientMessage;
import com.order.management.system.orderingmicroservice.util.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClientPresenter {

    @Autowired
    private ObjectMapper objectMapper;

    public Client convert(Dto dto) {
        String document = normalizeDocument(dto.getDocument());

        return new Client(
                document,
                dto.getName(),
                dto.getEmail(),
                dto.getCellphone()
        );
    }

    private String normalizeDocument(String document) {
        if (document == null || document.trim().isEmpty()) {
            throw new IllegalArgumentException(MessageUtil.getMessage("MESSAGE_CLIENT_DOCUMENT_OBRIGATORY"));
        }

        document = document.replaceAll("[.-]", "");

        if (document.length() == 11) {
            document = document.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
        } else if (document.length() == 14) {
            document = document.replaceAll("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})", "$1.$2.$3/$4-$5");
        }

        return document;
    }

    public ClientMessage convert(Order order) {
        ClientMessage message = new ClientMessage();

        message.setOrderId(order.getId());
        message.setDocument(order.getClient().getDocument());
        message.setName(order.getClient().getName());
        message.setEmail(order.getClient().getEmail());
        message.setCellphone(order.getClient().getCellphone());
        message.setStreet(order.getShipment().getStreet());
        message.setNeighborhood(order.getShipment().getNeighborhood());
        message.setNumber(order.getShipment().getNumber());
        message.setComplement(order.getShipment().getComplement());
        message.setCep(order.getShipment().getCep());
        message.setCity(order.getShipment().getCity());
        message.setState(order.getShipment().getState());

        return message;
    }

    public JsonNode convert(ClientMessage client) {
        ObjectNode json = objectMapper.createObjectNode();

        json.put("nome", client.getName());
        json.put("ativo", true);
        json.put("email", client.getEmail());
        json.put("tipoPagamentoPreferencial", "CARTAO_CREDITO");
        json.put("telefone", client.getCellphone());

        ArrayNode addresses = objectMapper.createArrayNode();
        ObjectNode address = objectMapper.createObjectNode();

        address.put("tipoEndereco", "OUTRO");
        address.put("logradouro", client.getStreet());
        address.put("numero", client.getNumber());
        address.put("bairro", client.getNeighborhood());
        address.put("cidade", client.getCity());
        address.put("estado", client.getState());
        address.put("complemento", client.getComplement());
        address.put("cep", client.getCep());

        addresses.add(address);
        json.set("clienteEnderecosDto", addresses);

        ArrayNode documents = objectMapper.createArrayNode();
        ObjectNode document = objectMapper.createObjectNode();

        document.put("documento", client.getDocument());
        document.put("tipoDocumentoCliente", getDocumentType(client.getDocument()));

        documents.add(document);

        json.set("clienteDocumentosDto", documents);

        return json;
    }

    private String getDocumentType(String document) {
        String aux = document.replaceAll("[.-]", "");

        if (aux.length() == 11) {
            return "CPF";
        }

        if (aux.length() == 14) {
            return "CNPJ";
        }

        return "RNE";
    }
}
