package com.vielendanke.orderservice.command.handler;

import com.vielendanke.core.events.OrderApprovedEvent;
import com.vielendanke.orderservice.core.events.OrderCreatedEvent;
import com.vielendanke.core.model.Order;
import com.vielendanke.orderservice.core.repository.OrderRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderEventsHandler {

    private final OrderRepository repository;

    @Autowired
    public OrderEventsHandler(OrderRepository repository) {
        this.repository = repository;
    }

    @EventHandler
    public void handle(OrderCreatedEvent event) {
        Order order = new Order();

        BeanUtils.copyProperties(event, order);

        repository.save(order);
    }

    @EventHandler
    public void handle(OrderApprovedEvent event) {
        boolean isUpdated = repository.changeOrderStatus(event.getOrderId(), event.getOrderStatus());

        if (!isUpdated) {
            // todo something
            return;
        }
    }
}
