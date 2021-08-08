package com.vielendanke.orderservice.command.handler;

import com.mongodb.client.result.UpdateResult;
import com.vielendanke.core.events.OrderApprovedEvent;
import com.vielendanke.orderservice.core.events.OrderCreatedEvent;
import com.vielendanke.orderservice.core.events.OrderRejectedEvent;
import com.vielendanke.orderservice.core.model.Order;
import com.vielendanke.orderservice.core.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderEventsHandler {

    private final OrderRepository repository;
    private final MongoTemplate mongoTemplate;

    @Autowired
    public OrderEventsHandler(OrderRepository repository, MongoTemplate mongoTemplate) {
        this.repository = repository;
        this.mongoTemplate = mongoTemplate;
    }

    @EventHandler
    public void handle(OrderCreatedEvent event) {
        Order order = new Order();

        BeanUtils.copyProperties(event, order);

        repository.save(order);
    }

    @EventHandler
    public void handle(OrderApprovedEvent event) {
        boolean isUpdated = updateOrderStatus(event.getOrderId(), event.getOrderStatus().name());
        log.info(String.format("Rejected event is updated: %b", isUpdated));
    }

    @EventHandler
    public void handle(OrderRejectedEvent event) {
        boolean isUpdated = updateOrderStatus(event.getOrderId(), event.getOrderStatus().name());
        log.info(String.format("Rejected event is updated: %b", isUpdated));
    }

    private boolean updateOrderStatus(String orderId, String orderStatus) {
        Query query = new Query();
        query.addCriteria(Criteria.where("orderId").is(orderId));
        Update update = new Update();
        update.set("orderStatus", orderStatus);
        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, Order.class);

        return updateResult.wasAcknowledged() && updateResult.getModifiedCount() != 0;
    }
}
