package com.vielendanke.paymentservice.command;

import com.vielendanke.core.commands.ProcessPaymentCommand;
import com.vielendanke.core.events.PaymentProcessedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

@Aggregate
public class PaymentAggregate {

    @AggregateIdentifier
    private String paymentId;
    private String orderId;

    public PaymentAggregate() {}

    @CommandHandler
    public PaymentAggregate(ProcessPaymentCommand command) {
        PaymentProcessedEvent event = PaymentProcessedEvent.builder()
                .orderId(command.getOrderId())
                .paymentId(command.getPaymentId())
                .build();

        if (event.getOrderId() == null || event.getPaymentId() == null ||
                event.getOrderId().isBlank() || event.getPaymentId().isBlank()) {
            throw new IllegalArgumentException("Payment processed command is not fulfilled correctly");
        }

        BeanUtils.copyProperties(command, event);

        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on(PaymentProcessedEvent event) {
        this.orderId = event.getOrderId();
        this.paymentId = event.getPaymentId();
    }
}
