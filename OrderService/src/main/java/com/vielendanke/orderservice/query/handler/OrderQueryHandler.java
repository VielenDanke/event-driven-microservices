package com.vielendanke.orderservice.query.handler;

import com.vielendanke.orderservice.core.model.Order;
import com.vielendanke.orderservice.core.model.OrderSummary;
import com.vielendanke.orderservice.core.repository.OrderRepository;
import com.vielendanke.orderservice.query.FindOrderQuery;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class OrderQueryHandler {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderQueryHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @QueryHandler
    public OrderSummary findOrder(FindOrderQuery query) {
        Optional<Order> orderById = orderRepository.findById(query.getOrderId());
        if (orderById.isPresent()) {
            Order order = orderById.get();
            return new OrderSummary(order.getOrderId(), order.getOrderStatus(), "");
        }
        return null;
    }
}
