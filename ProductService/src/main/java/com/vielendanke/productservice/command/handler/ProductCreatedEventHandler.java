package com.vielendanke.productservice.command.handler;

import com.vielendanke.productservice.core.entity.Product;
import com.vielendanke.productservice.core.events.ProductCreatedEvent;
import com.vielendanke.productservice.core.repository.ProductRepository;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@ProcessingGroup(value = "product-group")
public class ProductCreatedEventHandler {

    private final ProductRepository productRepository;

    @Autowired
    public ProductCreatedEventHandler(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @EventHandler
    public void on(ProductCreatedEvent productCreatedEvent) {
        Product product = new Product();
        BeanUtils.copyProperties(productCreatedEvent, product);
        productRepository.save(product);
    }
}
