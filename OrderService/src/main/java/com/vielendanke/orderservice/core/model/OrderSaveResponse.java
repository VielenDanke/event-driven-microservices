package com.vielendanke.orderservice.core.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class OrderSaveResponse {

    @JsonProperty("orderId")
    public String orderId;
}
