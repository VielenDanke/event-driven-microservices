package com.vielendanke.productservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

public class ProductResponseModel {

    @JsonProperty(value = "title")
    public String title;
    @JsonProperty(value = "price")
    public BigDecimal price;
    @JsonProperty(value = "quantity")
    public Integer quantity;
}
