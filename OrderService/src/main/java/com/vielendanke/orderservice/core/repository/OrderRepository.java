package com.vielendanke.orderservice.core.repository;

import com.vielendanke.orderservice.core.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<Order, String> {
}
