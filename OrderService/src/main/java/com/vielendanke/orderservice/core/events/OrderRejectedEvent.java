package com.vielendanke.orderservice.core.events;

import com.vielendanke.core.model.OrderStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderRejectedEvent {

    private final String orderId;
    private final String reason;
    private final OrderStatus orderStatus;
}
