package com.order.management.system.orderingmicroservice.frameworks.external.interfaces.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.order.management.system.orderingmicroservice.entities.Client;
import com.order.management.system.orderingmicroservice.interfaceadapters.presenters.ClientPresenter;
import com.order.management.system.orderingmicroservice.interfaceadapters.presenters.messages.ClientMessage;
import com.order.management.system.orderingmicroservice.util.MessageUtil;
import com.order.management.system.orderingmicroservice.util.exception.ExternalInterfaceException;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ClientWeb {

    @Autowired
    private ClientWebInterface clientWebInterface;

    @Autowired
    private ClientPresenter presenter;

    public Optional<Client> findClientByDocument(String document) throws ExternalInterfaceException {
        try {
            JsonNode client = clientWebInterface.findByDocument(document);

            return Optional.of(new Client(document,
                    client.get("nome").asText(),
                    client.get("email").asText(),
                    client.get("telefone").asText())
            );
        } catch (FeignException exception) {
            int statusCode = exception.status();

            if (statusCode == 404) {
                return Optional.empty();
            }

            throw new ExternalInterfaceException(MessageUtil.getMessage("LOG_EXTERNAL_SERVICE_CLIENT", document), exception);
        } catch (Exception exception) {
            throw new ExternalInterfaceException(MessageUtil.getMessage("LOG_EXTERNAL_SERVICE_CLIENT", document), exception);
        }
    }

    public void insert(ClientMessage message) throws ExternalInterfaceException {
        JsonNode payload = presenter.convert(message);

        try {
            clientWebInterface.insert(payload);
        } catch (FeignException feignException) {
            throw new ExternalInterfaceException(MessageUtil.getMessage("LOG_EXTERNAL_SERVICE_CLIENT_INSERT", message.getDocument()), feignException);
        } catch (Exception exception) {
            throw new ExternalInterfaceException(MessageUtil.getMessage("LOG_EXTERNAL_SERVICE_CLIENT", message.getDocument()), exception);
        }
    }
}
