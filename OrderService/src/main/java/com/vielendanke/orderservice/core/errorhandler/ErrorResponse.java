package com.vielendanke.orderservice.core.errorhandler;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class ErrorResponse {

    @JsonProperty("timestamp")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "YYYY-mm-dd HH:MM:SS")
    public final Date timestamp = new Date();

    @JsonProperty("message")
    public final String message;

    public ErrorResponse(String message) {
        this.message = message;
    }
}
