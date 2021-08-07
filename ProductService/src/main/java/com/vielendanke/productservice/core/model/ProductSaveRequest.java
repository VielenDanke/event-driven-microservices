package com.vielendanke.productservice.core.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

public class ProductSaveRequest {

    @JsonProperty(value = "title")
    @NotBlank(message = "Title should not be blank")
    public String title;

    @JsonProperty(value = "price")
    @Min(value = 1, message = "Price cannot be less than 1")
    public BigDecimal price;

    @JsonProperty(value = "quantity")
    @Min(value = 1, message = "Quantity cannot be less than 1")
    @Max(value = 5, message = "Quantity cannot be larger than 5")
    public Integer quantity;
}
