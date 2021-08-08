package com.vielendanke.orderservice.core.repository.impl;

import com.mongodb.client.result.UpdateResult;
import com.vielendanke.core.model.Order;
import com.vielendanke.core.model.OrderStatus;
import com.vielendanke.orderservice.core.repository.OrderRepository;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
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
    public boolean changeOrderStatus(String orderId, OrderStatus status) {
        UpdateResult result = shopDatabase.getCollection("orders").updateOne(
                new Document("orderId", orderId), new Document("$set", new Document("orderStatus", status.name())));

        return result.wasAcknowledged() && result.getModifiedCount() != 0;
    }

    @Override
    public void save(Order order) {
        shopDatabase.getCollection("orders").insertOne(order.toDocument());
    }
}
