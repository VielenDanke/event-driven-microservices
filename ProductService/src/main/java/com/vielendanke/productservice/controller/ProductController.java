package com.vielendanke.productservice.controller;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCursor;
import com.vielendanke.productservice.model.ProductResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final MongoClient mongoClient;

    @Autowired
    public ProductController(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    @PostMapping
    public ResponseEntity<Object> save() {
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseModel>> findAll() {
        List<ProductResponseModel> rsp = new ArrayList<>();
        MongoCursor<ProductResponseModel> cursor = mongoClient.getDatabase("shop").getCollection("products").find(ProductResponseModel.class).cursor();
        while (cursor.hasNext()) {
            rsp.add(cursor.next());
        }
        return ResponseEntity.ok(rsp);
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
