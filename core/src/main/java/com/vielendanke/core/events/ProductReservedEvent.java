package com.vielendanke.core.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductReservedEvent {

    private String productId;
    private int quantity;
    private String orderId;
    private String userId;
}
