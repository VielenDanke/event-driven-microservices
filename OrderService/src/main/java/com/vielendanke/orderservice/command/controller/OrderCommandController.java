package com.vielendanke.orderservice.command.controller;

import com.vielendanke.orderservice.command.CreateOrderCommand;
import com.vielendanke.orderservice.core.model.OrderSaveRequest;
import com.vielendanke.orderservice.core.model.OrderSaveResponse;
import com.vielendanke.core.model.OrderStatus;
import com.vielendanke.orderservice.core.model.OrderSummary;
import com.vielendanke.orderservice.query.FindOrderQuery;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
@Slf4j
public class OrderCommandController {

    private final CommandGateway gateway;
    private final QueryGateway queryGateway;

    @Autowired
    public OrderCommandController(CommandGateway gateway, QueryGateway queryGateway) {
        this.gateway = gateway;
        this.queryGateway = queryGateway;
    }

    @PostMapping
    public ResponseEntity<OrderSummary> save(@RequestBody @Valid OrderSaveRequest request) {
        CreateOrderCommand command = CreateOrderCommand.builder()
                .orderId(UUID.randomUUID().toString())
                .orderStatus(OrderStatus.CREATED)
                .productId(request.productId)
                .quantity(request.quantity)
                .addressId(request.addressId)
                .userId("27b95829-4f3f-4ddf-8983-151ba010e35b")
                .build();

        try (SubscriptionQueryResult<OrderSummary, OrderSummary> queryResult = queryGateway.subscriptionQuery(
                new FindOrderQuery(command.getOrderId()),
                ResponseTypes.instanceOf(OrderSummary.class),
                ResponseTypes.instanceOf(OrderSummary.class)
        )) {
            gateway.sendAndWait(command);

            return ResponseEntity.status(HttpStatus.CREATED).body(queryResult.updates().blockFirst());
        } catch (Exception e) {
            log.error("Error in method SAVE");
        }
        return ResponseEntity.internalServerError().build();
    }
}
