package com.vielendanke.productservice.command;

import com.vielendanke.core.commands.CancelProductReservationCommand;
import com.vielendanke.core.commands.ReserveProductCommand;
import com.vielendanke.core.events.ProductReservationCancelEvent;
import com.vielendanke.core.events.ProductReservedEvent;
import com.vielendanke.productservice.core.events.ProductCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

@Aggregate(snapshotFilter = "productSnapshotTriggerDefinition")
@Slf4j
public class ProductAggregate {

    @AggregateIdentifier
    private String productId;
    private String title;
    private BigDecimal price;
    private Integer quantity;

    public ProductAggregate() {
    }

    @CommandHandler
    public ProductAggregate(CreateProductCommand createProductCommand) {
        // validate create product command
        if (createProductCommand.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price cannot be less or equal than zero");
        }
        if (createProductCommand.getTitle().isBlank()) {
            throw new IllegalArgumentException("Title cannot be blank");
        }
        ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent();

        // not an optimized, might be resource expensive
        BeanUtils.copyProperties(createProductCommand, productCreatedEvent);

        AggregateLifecycle.apply(productCreatedEvent);
    }

    @CommandHandler
    public void handle(ReserveProductCommand command) {
        if (quantity < command.getQuantity()) {
            throw new IllegalArgumentException("Insufficient number items in stock");
        }
        ProductReservedEvent event = new ProductReservedEvent();

        BeanUtils.copyProperties(command, event);

        log.info(String.format("Reserve for product ID %s is processing", command.getProductId()));

        AggregateLifecycle.apply(event);
    }

    @CommandHandler
    public void handle(CancelProductReservationCommand command) {
        ProductReservationCancelEvent event = ProductReservationCancelEvent.builder()
                .reason(command.getReason())
                .userId(command.getUserId())
                .productId(command.getProductId())
                .orderId(command.getOrderId())
                .quantity(command.getQuantity())
                .build();

        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on(ProductReservedEvent event) {
        this.quantity -= event.getQuantity();
    }

    @EventSourcingHandler
    public void on(ProductCreatedEvent productCreatedEvent) {
        this.productId = productCreatedEvent.getProductId();
        this.title = productCreatedEvent.getTitle();
        this.price = productCreatedEvent.getPrice();
        this.quantity = productCreatedEvent.getQuantity();
    }

    @EventSourcingHandler
    public void on(ProductReservationCancelEvent event) {
        this.quantity += event.getQuantity();
    }
}
