package com.vielendanke.orderservice.core.model;

import com.vielendanke.core.model.OrderStatus;
import lombok.*;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = {"orderId","productId","addressId"})
@ToString
@Document
public class Order {

    @Id
    @BsonId
    @Field("orderId")
    private String orderId;

    @Field("userId")
    @BsonProperty("userId")
    private String userId;

    @Field("productId")
    @BsonProperty("productId")
    private String productId;

    @Field("quantity")
    @BsonProperty("quantity")
    private int quantity;

    @Field("addressId")
    @BsonProperty("addressId")
    private String addressId;

    @Field("orderStatus")
    @BsonProperty("orderStatus")
    private OrderStatus orderStatus;
}
