package com.vielendanke.productservice.command.handler;

import com.vielendanke.core.events.ProductReservationCancelEvent;
import com.vielendanke.core.events.ProductReservedEvent;
import com.vielendanke.productservice.core.entity.Product;
import com.vielendanke.productservice.core.events.ProductCreatedEvent;
import com.vielendanke.productservice.core.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.ResetHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@ProcessingGroup(value = "product-group")
@Slf4j
public class ProductEventsHandler {

    private final ProductRepository productRepository;

    @Autowired
    public ProductEventsHandler(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @ExceptionHandler(resultType = IllegalArgumentException.class)
    public void handle(IllegalArgumentException exception) {
        log.error(String.format("Exception in created command event: %s", exception.getLocalizedMessage()));
    }

    @ExceptionHandler
    public void handle(Exception exception) throws Exception {
        log.error(String.format("Unexpected exception is occurred: %s", exception.getLocalizedMessage()));
        throw exception;
    }

    @EventHandler
    public void on(ProductReservedEvent event) {
        productRepository.updateQuantity(event.getProductId(), -event.getQuantity());
    }

    @EventHandler
    public void on(ProductCreatedEvent productCreatedEvent) {
        Product product = new Product();

        BeanUtils.copyProperties(productCreatedEvent, product);

        productRepository.save(product);
    }

    @EventHandler
    public void on(ProductReservationCancelEvent event) {
        productRepository.updateQuantity(event.getProductId(), event.getQuantity());
    }

    @ResetHandler
    public void reset() {
        productRepository.deleteAll();
    }
}
