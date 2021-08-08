package com.vielendanke.paymentservice.core.repository;

import com.vielendanke.core.model.Payment;

public interface PaymentRepository {

    void save(Payment payment);
}
