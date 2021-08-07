package com.vielendanke.orderservice.core.repository.impl;

import com.vielendanke.orderservice.core.model.Order;
import com.vielendanke.orderservice.core.repository.OrderRepository;
import com.mongodb.client.MongoDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private final MongoDatabase shopDatabase;

    @Autowired
    public OrderRepositoryImpl(@Qualifier("shopDatabase") MongoDatabase shopDatabase) {
        this.shopDatabase = shopDatabase;
    }

    @Override
    public void save(Order order) {
        shopDatabase.getCollection("orders").insertOne(order.toDocument());
    }
}
