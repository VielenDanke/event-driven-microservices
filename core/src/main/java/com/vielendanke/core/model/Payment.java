package com.vielendanke.core.model;

import lombok.Builder;
import lombok.Data;
import org.bson.Document;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

@Builder
@Data
public class Payment {

    @BsonId
    private final String paymentId;

    @BsonProperty("orderId")
    private final String orderId;

    public Document toDocument() {
        return new Document()
                .append("paymentId", paymentId)
                .append("orderId", orderId);
    }
}
