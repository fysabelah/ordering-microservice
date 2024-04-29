package com.order.management.system.orderingmicroservice.interfaceadapters.gateways;

import com.order.management.system.orderingmicroservice.entities.Client;
import com.order.management.system.orderingmicroservice.entities.Order;
import com.order.management.system.orderingmicroservice.frameworks.db.ItemRepository;
import com.order.management.system.orderingmicroservice.frameworks.db.OrderRepository;
import com.order.management.system.orderingmicroservice.util.MessageUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class OrderGateway {

    @Resource
    private OrderRepository repository;

    @Resource
    private ItemRepository itemRepository;

    @Resource
    private ClientGateway clientGateway;

    public Order findById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException(MessageUtil.getMessage("MESSAGE_MISSING_ATTRIBUTES"));
        }

        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(MessageUtil.getMessage("MESSAGE_ID_NOT_FOUND", "Pedido", id.toString())));
    }

    public Optional<Order> findByIdAndClientDocument(Integer orderId, String clientDocument) {
        if (orderId == null || clientDocument == null || clientDocument.trim().isEmpty()) {
            throw new IllegalArgumentException(MessageUtil.getMessage("MESSAGE_MISSING_ATTRIBUTES"));
        }

        return repository.findByIdAndClientDocument(orderId, clientDocument);
    }

    public void update(Order order) {
        repository.save(order);
    }

    public Order insert(Order order) {
        Optional<Client> client = clientGateway.findById(order.getClient().getDocument());

        if (client.isPresent()) {
            order.setClient(client.get());
        }

        order = repository.save(order);

        return order;
    }
}
