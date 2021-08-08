package com.vielendanke.orderservice.saga;

import com.vielendanke.core.commands.ProcessPaymentCommand;
import com.vielendanke.core.commands.ReserveProductCommand;
import com.vielendanke.core.events.OrderApprovedEvent;
import com.vielendanke.core.events.PaymentProcessedEvent;
import com.vielendanke.core.events.ProductReservedEvent;
import com.vielendanke.core.model.User;
import com.vielendanke.core.query.FetchUserPaymentDetailsQuery;
import com.vielendanke.orderservice.command.ApproveOrderCommand;
import com.vielendanke.orderservice.core.events.OrderCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.SagaLifecycle;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Saga
@Slf4j
public class OrderSaga {

    @Autowired
    private transient CommandGateway commandGateway;
    @Autowired
    private transient QueryGateway queryGateway;

    @StartSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderCreatedEvent event) {
        ReserveProductCommand reserveProductCommand = ReserveProductCommand.builder()
                .orderId(event.getOrderId())
                .productId(event.getProductId())
                .quantity(event.getQuantity())
                .userId(event.getUserId())
                .build();

        log.info(
                String.format(
                        "Order created event for product ID %s. Order ID %s",
                        event.getProductId(),
                        event.getOrderId()
                )
        );

        commandGateway.send(reserveProductCommand, ((commandMessage, commandResultMessage) -> {
            if (commandResultMessage.isExceptional()) {
                // Start compensating transaction
            }
        }));
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(ProductReservedEvent event) {
        log.info(String.format("Product with ID %s is reserved. Order ID %s", event.getProductId(), event.getOrderId()));
        // Process user payment
        String userId = event.getUserId();

        FetchUserPaymentDetailsQuery userDetailsQuery = new FetchUserPaymentDetailsQuery(userId);

        try {
            User fetchedUser = queryGateway.query(userDetailsQuery, ResponseTypes.instanceOf(User.class))
                    .join();
            if (fetchedUser == null) {
                // start compensating transaction
                return;
            }
            log.info(String.format("User with name %s %s fetched successfully",
                    fetchedUser.getFirstName(), fetchedUser.getLastName()));

            ProcessPaymentCommand paymentCommand = ProcessPaymentCommand.builder()
                    .orderId(event.getOrderId())
                    .paymentDetails(fetchedUser.getPaymentDetails())
                    .paymentId(UUID.randomUUID().toString())
                    .build();

            String result = commandGateway.sendAndWait(paymentCommand, 10, TimeUnit.SECONDS);

            if (result == null || result.isBlank()) {
                // start compensating transaction
                return;
            }
            log.info(String.format("Payment processed successfully. Payment ID %s", result));
        } catch (Exception e) {
            log.error(String.format("Error %s during fetching the user details with user ID %s", e.getLocalizedMessage(), userId));
            // start compensating transaction
            return;
        }
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(PaymentProcessedEvent event) {
        // send a command to finish the transaction
        ApproveOrderCommand command = new ApproveOrderCommand(event.getOrderId());

        log.info(String.format("Payment %s is processed", event.getOrderId()));

        commandGateway.send(command);
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderApprovedEvent event) {
        log.info(String.format("Order with ID %s is complete", event.getOrderId()));
//        SagaLifecycle.end();
    }
}