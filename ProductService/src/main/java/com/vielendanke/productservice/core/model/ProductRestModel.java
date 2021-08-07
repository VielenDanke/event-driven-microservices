package com.vielendanke.productservice.core.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vielendanke.productservice.core.entity.Product;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
public class ProductRestModel {

    @JsonProperty("id")
    public String id;

    @JsonProperty("title")
    public String title;

    @JsonProperty("quantity")
    public Integer quantity;

    @JsonProperty("price")
    public BigDecimal price;

    public static ProductRestModel toRestModel(Product product) {
        return new ProductRestModel(
                product.getId(),
                product.getTitle(),
                product.getQuantity(),
                product.getPrice()
        );
    }
}
