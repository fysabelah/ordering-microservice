package com.order.management.system.orderingmicroservice.interfaceadapters.gateways;

import com.order.management.system.orderingmicroservice.entities.Client;
import com.order.management.system.orderingmicroservice.frameworks.db.ClientRepository;
import com.order.management.system.orderingmicroservice.util.MessageUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientGateway {

    @Resource
    private ClientRepository clientRepository;

    public Optional<Client> findById(String id) {
        if (id == null) {
            throw new IllegalArgumentException(MessageUtil.getMessage("MESSAGE_MISSING_ATTRIBUTES"));
        }

        return clientRepository.findById(id);
    }
}
