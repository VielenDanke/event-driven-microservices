package com.vielendanke.productservice.command.controller;

import com.vielendanke.productservice.command.CreateProductCommand;
import com.vielendanke.productservice.core.model.ProductSaveRequest;
import com.vielendanke.productservice.core.model.ProductSaveResponse;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductCommandController {

    private final CommandGateway commandGateway;

    @PostMapping
    public ResponseEntity<ProductSaveResponse> save(@RequestBody @Valid ProductSaveRequest request) {
        CreateProductCommand productCommand = CreateProductCommand.builder()
                .price(request.price)
                .title(request.title)
                .quantity(request.quantity)
                .productId(UUID.randomUUID().toString())
                .build();
        String returnValue = commandGateway.sendAndWait(productCommand);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ProductSaveResponse(returnValue));
    }
}
