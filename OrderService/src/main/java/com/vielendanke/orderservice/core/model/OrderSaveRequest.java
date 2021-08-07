package com.vielendanke.orderservice.core.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

public class OrderSaveRequest {

    @NotBlank(message = "Product ID should not be blank")
    public String productId;

    @Min(value = 1, message = "Quantity should not be less than 1")
    @Max(value = 5, message = "Quantity should not be greater than 5")
    public int quantity;

    @NotBlank(message = "Address ID should not be blank")
    public String addressId;
}
