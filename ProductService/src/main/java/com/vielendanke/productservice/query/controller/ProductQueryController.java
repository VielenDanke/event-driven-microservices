package com.vielendanke.productservice.query.controller;

import com.vielendanke.productservice.core.entity.Product;
import com.vielendanke.productservice.core.model.ProductRestModel;
import com.vielendanke.productservice.query.FindProductQuery;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductQueryController {

    private final QueryGateway queryGateway;

    @Autowired
    public ProductQueryController(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }

    @GetMapping
    public ResponseEntity<List<ProductRestModel>> findAll() {
        FindProductQuery findProductQuery = new FindProductQuery();
        List<ProductRestModel> all = queryGateway.query(findProductQuery, ResponseTypes.multipleInstancesOf(ProductRestModel.class))
                .join();
        return ResponseEntity.ok(all);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> findById(@PathVariable(name = "id") String id) {
        return ResponseEntity.ok().build();
    }
}
