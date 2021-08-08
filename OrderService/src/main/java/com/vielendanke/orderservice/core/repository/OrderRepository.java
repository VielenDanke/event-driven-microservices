package com.vielendanke.orderservice.core.repository;

import com.vielendanke.core.model.Order;
import com.vielendanke.core.model.OrderStatus;

public interface OrderRepository {

    void save(Order order);

    boolean changeOrderStatus(String orderId, OrderStatus status);
}
