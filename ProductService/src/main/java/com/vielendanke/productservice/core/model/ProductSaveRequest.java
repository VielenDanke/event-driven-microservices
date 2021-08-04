package com.vielendanke.productservice.core.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class ProductSaveRequest {

    @JsonProperty(value = "title")
    public String title;
    @JsonProperty(value = "price")
    public BigDecimal price;
    @JsonProperty(value = "quantity")
    public Integer quantity;
}
