package com.order.management.system.orderingmicroservice.frameworks.external.messaging.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.order.management.system.orderingmicroservice.entities.Order;
import com.order.management.system.orderingmicroservice.entities.StatusHistory;
import com.order.management.system.orderingmicroservice.interfaceadapters.gateways.OrderGateway;
import com.order.management.system.orderingmicroservice.interfaceadapters.presenters.messages.StatusMessage;
import com.order.management.system.orderingmicroservice.util.MessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatusFallbackConsume {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatusFallbackConsume.class);

    @Autowired
    private StatusPublishMessage statusPublishMessage;

    @Autowired
    private OrderGateway orderGateway;

    @RabbitListener(queues = "q.fall-back-process-status")
    public void handleStatusMapError(StatusMessage statusMessage) throws JsonProcessingException {
        try{
            Order order = orderGateway.findByIdWithStatusHistory(statusMessage.getOrderId());

            boolean isDuplicatedMessage = false;

            for (StatusHistory history: order.getStatusHistory()){
                if (history.getStatus().equals(statusMessage.getStatus())) {
                    isDuplicatedMessage = true;

                    break;
                }
            }

            if (!isDuplicatedMessage) {
                statusPublishMessage.sendMessage(statusMessage);
            } else {
                LOGGER.info(MessageUtil.getMessage("LOG_DISCART_MESSAGE", " status já atualizado - mensagem duplicada"));
            }
        } catch (MessageConversionException e) {
            LOGGER.info(MessageUtil.getMessage("LOG_ERROR_CONVERSION_JSON"));
        }
    }
}
