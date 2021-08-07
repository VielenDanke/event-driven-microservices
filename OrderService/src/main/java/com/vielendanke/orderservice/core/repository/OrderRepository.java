package com.vielendanke.orderservice.core.repository;

import com.vielendanke.orderservice.core.model.Order;

public interface OrderRepository {

    void save(Order order);
}
