package com.vielendanke.orderservice.core.model;

import com.vielendanke.core.model.OrderStatus;
import lombok.Value;

@Value
public class OrderSummary {

    String orderId;
    OrderStatus orderStatus;
    String reason;
}
