package com.vielendanke.productservice.controller;

import com.vielendanke.productservice.command.CreateProductCommand;
import com.vielendanke.productservice.core.model.ProductSaveRequest;
import com.vielendanke.productservice.core.model.ProductSaveResponse;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final CommandGateway commandGateway;

    @PostMapping
    public ResponseEntity<ProductSaveResponse> save(@RequestBody ProductSaveRequest request) {
        CreateProductCommand productCommand = CreateProductCommand.builder()
                .price(request.price)
                .title(request.title)
                .quantity(request.quantity)
                .productId(UUID.randomUUID().toString())
                .build();
        String returnValue = commandGateway.sendAndWait(productCommand);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ProductSaveResponse(returnValue));
    }

    @GetMapping
    public ResponseEntity<List<Object>> findAll() {
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<Object> update() {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Object> delete() {
        return ResponseEntity.ok().build();
    }
}
