package com.vielendanke.productservice.core.errorhandler;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@AllArgsConstructor
@Data
public class ErrorMessage {

    @JsonProperty("currentTimestamp")
    @JsonFormat(pattern = "YYYY-mm-dd HH:MM:SS", shape = JsonFormat.Shape.STRING)
    private final Date timestamp = new Date();

    @JsonProperty("message")
    private final String message;
}
