package com.order.management.system.orderingmicroservice.frameworks.external.interfaces.product;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.order.management.system.orderingmicroservice.interfaceadapters.presenters.ProductsPresenter;
import com.order.management.system.orderingmicroservice.interfaceadapters.presenters.messages.StockMessage;
import com.order.management.system.orderingmicroservice.util.exception.ExternalInterfaceException;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InventoryWeb {

    @Autowired
    private InventoryWebInterface inventoryWebInterface;

    @Autowired
    private ProductsPresenter presenter;

    // TODO NÃO FOI POSSIVEL VALIDAR. Vai ter que voltar para arrumar os metódos
    public void confirmReservation(StockMessage stockMessage) throws ExternalInterfaceException {
        try {
            ArrayNode json = presenter.convert(stockMessage);

            ArrayNode body = inventoryWebInterface.confirmReservation(json);
        } catch (FeignException exception) {
            throw new ExternalInterfaceException(exception);
        }
    }

    public void cancelReservation(StockMessage stockMessage) throws ExternalInterfaceException {
        try {
            ArrayNode json = presenter.convert(stockMessage);

            ArrayNode body = inventoryWebInterface.cancelReservation(json);
        } catch (FeignException exception) {
            throw new ExternalInterfaceException(exception);
        }
    }
}
