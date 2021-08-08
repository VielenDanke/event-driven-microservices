package com.vielendanke.paymentservice.core.repository.impl;

import com.mongodb.client.MongoDatabase;
import com.vielendanke.core.model.Payment;
import com.vielendanke.paymentservice.core.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class PaymentRepositoryImpl implements PaymentRepository {

    private final MongoDatabase shopDatabase;

    @Autowired
    public PaymentRepositoryImpl(@Qualifier("shopDatabase") MongoDatabase shopDatabase) {
        this.shopDatabase = shopDatabase;
    }

    @Override
    public void save(Payment payment) {
        shopDatabase.getCollection("payments").insertOne(payment.toDocument());
    }
}
