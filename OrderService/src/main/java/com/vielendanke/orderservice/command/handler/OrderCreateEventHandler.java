package com.vielendanke.orderservice.command.handler;

import com.vielendanke.orderservice.core.events.OrderCreatedEvent;
import com.vielendanke.orderservice.core.model.Order;
import com.vielendanke.orderservice.core.repository.OrderRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderCreateEventHandler {

    private final OrderRepository repository;

    @Autowired
    public OrderCreateEventHandler(OrderRepository repository) {
        this.repository = repository;
    }

    @EventHandler
    public void handle(OrderCreatedEvent event) {
        Order order = new Order();

        BeanUtils.copyProperties(event, order);

        repository.save(order);
    }
}
