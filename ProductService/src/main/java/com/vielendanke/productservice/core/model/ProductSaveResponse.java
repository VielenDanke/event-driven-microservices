package com.vielendanke.productservice.core.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductSaveResponse {

    @JsonProperty(value = "id")
    public String id;

    public ProductSaveResponse() {
    }

    public ProductSaveResponse(String id) {
        this.id = id;
    }
}
