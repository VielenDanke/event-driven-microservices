package com.vielendanke.orderservice.core.model;

import lombok.*;
import org.bson.Document;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = {"orderId","productId","addressId"})
@ToString
public class Order {

    @BsonId
    private String orderId;

    @BsonProperty("userId")
    private String userId;

    @BsonProperty("productId")
    private String productId;

    @BsonProperty("quantity")
    private int quantity;

    @BsonProperty("addressId")
    private String addressId;

    @BsonProperty("orderStatus")
    private OrderStatus orderStatus;

    public Document toDocument() {
        Map<String, Object> docMap = new HashMap<>();

        docMap.put("orderId", this.orderId);
        docMap.put("userId", this.userId);
        docMap.put("productId", this.productId);
        docMap.put("quantity", this.quantity);
        docMap.put("addressId", this.addressId);
        docMap.put("orderStatus", this.orderStatus.name());

        return new Document(docMap);
    }
}
