package com.vielendanke.productservice.query.handler;

import com.vielendanke.productservice.core.model.ProductRestModel;
import com.vielendanke.productservice.core.repository.ProductRepository;
import com.vielendanke.productservice.query.FindProductQuery;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductQueryHandler {

    private final ProductRepository productRepository;

    @Autowired
    public ProductQueryHandler(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @QueryHandler
    public List<ProductRestModel> findProducts(FindProductQuery findProductQuery) {
        return productRepository.findAll().stream().map(ProductRestModel::toRestModel).collect(Collectors.toList());
    }
}
