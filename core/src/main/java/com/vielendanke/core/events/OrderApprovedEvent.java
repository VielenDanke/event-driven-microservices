package com.vielendanke.core.events;

import com.vielendanke.core.model.OrderStatus;
import lombok.Value;

@Value
public class OrderApprovedEvent {

    String orderId;
    OrderStatus orderStatus = OrderStatus.APPROVED;
}
