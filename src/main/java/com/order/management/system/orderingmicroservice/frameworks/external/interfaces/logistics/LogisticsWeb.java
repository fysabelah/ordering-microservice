package com.order.management.system.orderingmicroservice.frameworks.external.interfaces.logistics;

import com.fasterxml.jackson.databind.JsonNode;
import com.order.management.system.orderingmicroservice.util.MessageUtil;
import com.order.management.system.orderingmicroservice.util.exception.ExternalInterfaceException;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LogisticsWeb {

    @Autowired
    private LogisticsWebInterface logisticsWebInterface;

    public void cancelDelivery(Integer orderId) throws ExternalInterfaceException {
        try {
            logisticsWebInterface.cancelDelivery(orderId.toString());
        } catch (FeignException exception) {
            throw new ExternalInterfaceException(MessageUtil.getMessage("LOG_EXTERNAL_SERVICE_LOGISTICS", orderId.toString()), exception);
        } catch (Exception exception) {
            throw new ExternalInterfaceException(MessageUtil.getMessage("LOG_GENERAL_EXCEPTION", "cancelar pedido no microsserviço de transporte " + orderId), exception);
        }
    }

    public void createDelivery(Integer orderId, String cep, JsonNode items) throws ExternalInterfaceException {
        try {
            logisticsWebInterface.createDelivery(orderId.toString(), cep, items);
        } catch (FeignException exception) {
            throw new ExternalInterfaceException(MessageUtil.getMessage("LOG_EXTERNAL_SERVICE_LOGISTICS", orderId.toString()), exception);
        } catch (Exception exception) {
            throw new ExternalInterfaceException(MessageUtil.getMessage("LOG_GENERAL_EXCEPTION", "criar pedido no microsserviço de transporte " + orderId), exception);
        }
    }
}
