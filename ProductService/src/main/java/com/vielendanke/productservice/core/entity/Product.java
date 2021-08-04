package com.vielendanke.productservice.core.entity;

import lombok.*;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(of = "id")
public class Product implements Serializable {

    private static final long serialVersionUID = -7071491716310035644L;

    @BsonId
    private String id;
    @BsonProperty(value = "title")
    private String title;
    @BsonProperty(value = "quantity")
    private Integer quantity;
    @BsonProperty(value = "price")
    private BigDecimal price;

    public Map<String, Object> transformToDocument() {
        Map<String, Object> doc = new HashMap<>();
        doc.put("id", id);
        doc.put("title", title);
        doc.put("quantity", quantity);
        doc.put("price", price);
        return doc;
    }
}
