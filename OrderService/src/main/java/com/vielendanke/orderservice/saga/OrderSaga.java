package com.vielendanke.orderservice.saga;

import com.vielendanke.core.commands.CancelProductReservationCommand;
import com.vielendanke.core.commands.ProcessPaymentCommand;
import com.vielendanke.core.commands.ReserveProductCommand;
import com.vielendanke.core.events.OrderApprovedEvent;
import com.vielendanke.core.events.PaymentProcessedEvent;
import com.vielendanke.core.events.ProductReservationCancelEvent;
import com.vielendanke.core.events.ProductReservedEvent;
import com.vielendanke.core.model.User;
import com.vielendanke.core.query.FetchUserPaymentDetailsQuery;
import com.vielendanke.orderservice.command.ApproveOrderCommand;
import com.vielendanke.orderservice.command.RejectOrderCommand;
import com.vielendanke.orderservice.core.events.OrderCreatedEvent;
import com.vielendanke.orderservice.core.events.OrderRejectedEvent;
import com.vielendanke.orderservice.core.model.OrderSummary;
import com.vielendanke.orderservice.query.FindOrderQuery;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.deadline.DeadlineManager;
import org.axonframework.deadline.annotation.DeadlineHandler;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Saga
@Slf4j
public class OrderSaga {

    private static final String PRODUCT_DEADLINE_NAME = "payment-processing-deadline";

    @Autowired
    private transient CommandGateway commandGateway;
    @Autowired
    private transient QueryGateway queryGateway;
    @Autowired
    private transient DeadlineManager deadlineManager;
    @Autowired
    private transient QueryUpdateEmitter queryUpdateEmitter;

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
                cancelProductReservation(event, "Fetching user is failed");
                return;
            }
            log.info(String.format("User with name %s %s fetched successfully",
                    fetchedUser.getFirstName(), fetchedUser.getLastName()));

            deadlineManager.schedule(
                    Duration.of(2, ChronoUnit.MINUTES), PRODUCT_DEADLINE_NAME, event);

            ProcessPaymentCommand paymentCommand = ProcessPaymentCommand.builder()
                    .orderId(event.getOrderId())
                    .paymentDetails(fetchedUser.getPaymentDetails())
                    .paymentId(UUID.randomUUID().toString())
                    .build();

            String result = commandGateway.sendAndWait(paymentCommand);

            if (result == null || result.isBlank()) {
                cancelProductReservation(event, "Result ID of payment command is empty or null");
                return;
            }
            log.info(String.format("Payment processed successfully. Payment ID %s", result));
        } catch (Exception e) {
            log.error(String.format("Error %s during fetching the user details with user ID %s", e.getLocalizedMessage(), userId));
            cancelProductReservation(event, String.format("Unexpected error occurred: %s", e.getLocalizedMessage()));
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

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(ProductReservationCancelEvent event) {
        // create and send reject order command
        RejectOrderCommand command = RejectOrderCommand.builder()
                .orderId(event.getOrderId())
                .reason(event.getReason())
                .build();

        commandGateway.send(command);
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderRejectedEvent event) {
        log.info(String.format("Order with ID %s successfully rejected", event.getOrderId()));
        queryUpdateEmitter.emit(
                FindOrderQuery.class,
                q -> true,
                new OrderSummary(event.getOrderId(), event.getOrderStatus(), event.getReason())
        );
    }

    @DeadlineHandler(deadlineName = PRODUCT_DEADLINE_NAME)
    public void handlePaymentDeadline(ProductReservedEvent event) {
        log.warn("Payment deadline event took place");
        cancelProductReservation(event, "Payment deadline took place");
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderApprovedEvent event) {
        log.info(String.format("Order with ID %s is complete", event.getOrderId()));
//        SagaLifecycle.end();
        queryUpdateEmitter.emit(
                FindOrderQuery.class,
                q -> true,
                new OrderSummary(event.getOrderId(), event.getOrderStatus(), "")
        );
    }

    private void cancelDeadline(String scheduleId) {
        deadlineManager.cancelSchedule(PRODUCT_DEADLINE_NAME, scheduleId);
    }

    private void cancelProductReservation(ProductReservedEvent event, String reason) {
        CancelProductReservationCommand command = CancelProductReservationCommand.builder()
                .reason(reason)
                .productId(event.getProductId())
                .orderId(event.getOrderId())
                .quantity(event.getQuantity())
                .userId(event.getUserId())
                .build();

        commandGateway.send(command);
    }
}