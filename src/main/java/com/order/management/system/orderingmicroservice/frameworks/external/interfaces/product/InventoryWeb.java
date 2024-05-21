package com.order.management.system.orderingmicroservice.frameworks.external.interfaces.product;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.order.management.system.orderingmicroservice.interfaceadapters.presenters.ProductsPresenter;
import com.order.management.system.orderingmicroservice.interfaceadapters.presenters.messages.StockMessage;
import com.order.management.system.orderingmicroservice.util.exception.ExternalInterfaceException;
import feign.FeignException;
import io.swagger.v3.core.util.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InventoryWeb {

    @Autowired
    private InventoryWebInterface inventoryWebInterface;

    @Autowired
    private ProductsPresenter presenter;

    public void confirmReservation(StockMessage stockMessage) throws ExternalInterfaceException {
        try {
            JsonNode json = presenter.convert(stockMessage);

            inventoryWebInterface.confirmReservation(json);
        } catch (FeignException exception) {
            throw new ExternalInterfaceException(exception);
        }
    }

    public void cancelReservation(StockMessage stockMessage) throws ExternalInterfaceException {
        try {
            JsonNode json = presenter.convert(stockMessage);

            inventoryWebInterface.cancelReservation(json);
        } catch (FeignException exception) {
            throw new ExternalInterfaceException(exception);
        }
    }
}
