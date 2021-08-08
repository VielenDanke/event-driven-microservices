package com.vielendanke.orderservice.command.controller;

import com.vielendanke.orderservice.command.CreateOrderCommand;
import com.vielendanke.orderservice.core.model.OrderSaveRequest;
import com.vielendanke.orderservice.core.model.OrderSaveResponse;
import com.vielendanke.core.model.OrderStatus;
import org.axonframework.commandhandling.gateway.CommandGateway;
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
public class OrderCommandController {

    private final CommandGateway gateway;

    @Autowired
    public OrderCommandController(CommandGateway gateway) {
        this.gateway = gateway;
    }

    @PostMapping
    public ResponseEntity<OrderSaveResponse> save(@RequestBody @Valid OrderSaveRequest request) {
        CreateOrderCommand command = CreateOrderCommand.builder()
                .orderId(UUID.randomUUID().toString())
                .orderStatus(OrderStatus.CREATED)
                .productId(request.productId)
                .quantity(request.quantity)
                .addressId(request.addressId)
                .userId("27b95829-4f3f-4ddf-8983-151ba010e35b")
                .build();
        String orderId = gateway.sendAndWait(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(new OrderSaveResponse(orderId));
    }
}
