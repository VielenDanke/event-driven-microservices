package com.vielendanke.orderservice.command;

import com.vielendanke.core.events.OrderApprovedEvent;
import com.vielendanke.orderservice.core.events.OrderCreatedEvent;
import com.vielendanke.core.model.OrderStatus;
import com.vielendanke.orderservice.core.events.OrderRejectedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

@Aggregate
public class OrderAggregate {

    @AggregateIdentifier
    private String orderId;
    private String productId;
    private String userId;
    private int quantity;
    private String addressId;
    private OrderStatus orderStatus;

    public OrderAggregate() {}

    @CommandHandler
    public OrderAggregate(CreateOrderCommand command) {
        OrderCreatedEvent event = new OrderCreatedEvent();

        BeanUtils.copyProperties(command, event);

        AggregateLifecycle.apply(event);
    }

    @CommandHandler
    public void handle(ApproveOrderCommand command) {
        // create and publish order approved event
        OrderApprovedEvent event = new OrderApprovedEvent(command.getOrderId());

        AggregateLifecycle.apply(event);
    }

    @CommandHandler
    public void handle(RejectOrderCommand command) {
        OrderRejectedEvent event = OrderRejectedEvent.builder()
                .orderId(command.getOrderId())
                .reason(command.getReason())
                .orderStatus(OrderStatus.REJECTED)
                .build();

        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on(OrderApprovedEvent event) {
        // change order status in axon aggregate order
        this.orderStatus = event.getOrderStatus();
    }

    @EventSourcingHandler
    public void on(OrderCreatedEvent event) {
        this.orderId = event.getOrderId();
        this.productId = event.getProductId();
        this.userId = event.getUserId();
        this.quantity = event.getQuantity();
        this.addressId = event.getAddressId();
        this.orderStatus = event.getOrderStatus();
    }

    @EventSourcingHandler
    public void on(OrderRejectedEvent event) {
        this.orderStatus = event.getOrderStatus();
    }
}
