package com.order.management.system.orderingmicroservice.frameworks.external.messaging.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.order.management.system.orderingmicroservice.interfaceadapters.controllers.OrderStatusController;
import com.order.management.system.orderingmicroservice.interfaceadapters.presenters.messages.StatusMessage;
import com.order.management.system.orderingmicroservice.util.MessageUtil;
import com.order.management.system.orderingmicroservice.util.exception.BusinessException;
import com.order.management.system.orderingmicroservice.util.exception.ProcessMessageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class StatusConsumeMessage {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatusConsumeMessage.class);

    @Autowired
    private OrderStatusController statusController;

    @Autowired
    private StatusPublishMessage statusPublishMessage;

    @RabbitListener(queues = "status.update")
    public void handleMessageProcessing(StatusMessage message) throws JsonProcessingException {
        try {
            boolean processed = statusController.updateStatus(message.getOrderId(), message.getStatus(), message.getMotive(), message.getProcessAt());

            if (!processed) {
                throw new ProcessMessageException(MessageUtil.getMessage("LOG_MESSAGE_ORDER_CANT_BE_UPDATED", message.getOrderId().toString()));
            }
        } catch (NoSuchElementException exception) {
            LOGGER.info(String.format("%s: %s", StatusConsumeMessage.class.getName(), MessageUtil.getMessage("LOG_DISCART_MESSAGE", "pedido não encontrado")));
        } catch (BusinessException exception) {
            LOGGER.info(String.format("%s: %s", StatusConsumeMessage.class.getName(), MessageUtil.getMessage("LOG_DISCART_MESSAGE", "pedido não pode ser cancelado")));
        }
    }
}
