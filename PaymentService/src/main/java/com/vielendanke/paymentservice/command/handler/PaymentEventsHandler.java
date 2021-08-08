package com.vielendanke.paymentservice.command.handler;

import com.vielendanke.core.events.PaymentProcessedEvent;
import com.vielendanke.core.model.Payment;
import com.vielendanke.paymentservice.core.repository.PaymentRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaymentEventsHandler {

    private final PaymentRepository repository;

    @Autowired
    public PaymentEventsHandler(PaymentRepository repository) {
        this.repository = repository;
    }

    @EventHandler
    public void handle(PaymentProcessedEvent event) {
        Payment payment = Payment.builder()
                .paymentId(event.getPaymentId())
                .orderId(event.getOrderId())
                .build();
        repository.save(payment);
    }
}
