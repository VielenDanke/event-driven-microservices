package com.vielendanke.productservice.command.handler;

import com.vielendanke.productservice.core.events.ProductCreatedEvent;
import com.vielendanke.productservice.core.model.ProductLookupEntity;
import com.vielendanke.productservice.core.repository.ProductLookupRepository;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@ProcessingGroup(value = "product-group")
public class ProductLookupEventHandler {

    private final ProductLookupRepository repository;

    @Autowired
    public ProductLookupEventHandler(ProductLookupRepository repository) {
        this.repository = repository;
    }

    @EventHandler
    public void on(ProductCreatedEvent productCreatedEvent) {
        ProductLookupEntity lookupEntity = new ProductLookupEntity();

        BeanUtils.copyProperties(productCreatedEvent, lookupEntity);

        repository.save(lookupEntity);
    }
}
