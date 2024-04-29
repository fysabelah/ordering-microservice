package com.order.management.system.orderingmicroservice.interfaceadapters.presenters;

import com.order.management.system.orderingmicroservice.entities.Item;
import com.order.management.system.orderingmicroservice.interfaceadapters.presenters.dtos.ItemDto;
import com.order.management.system.orderingmicroservice.interfaceadapters.presenters.messages.StockMessage;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductsPresenter {

    public Item convert(ItemDto dto) {
        return new Item(dto.getSku(), dto.getQuantity(), dto.getValue());
    }

    public List<Item> convertToEntity(List<ItemDto> dtos) {
        List<Item> items = new ArrayList<>();

        dtos.forEach(dto -> items.add(convert(dto)));

        return items;
    }

    public ItemDto convert(Item item) {
        return new ItemDto(item);
    }

    public List<ItemDto> convertToDto(List<Item> items) {
        List<ItemDto> dtos = new ArrayList<>();

        items.forEach(item -> dtos.add(convert(item)));

        return dtos;
    }

    public StockMessage convert(List<Item> items, int orderId) {
        StockMessage message = new StockMessage();

        message.setNumberOfAttempts(0);
        message.setOrderId(orderId);

        List<StockMessage.Product> products = new ArrayList<>();

        items.forEach(item -> {
            StockMessage.Product product = new StockMessage.Product(item.getSku(),
                    item.getQuantity(),
                    item.getValue());

            products.add(product);
        });

        message.setProducts(products);

        return message;
    }
}
