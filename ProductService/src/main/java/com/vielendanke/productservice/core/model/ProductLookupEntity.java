package com.vielendanke.productservice.core.model;

import lombok.*;
import org.bson.Document;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
public class ProductLookupEntity implements Serializable {

    private static final long serialVersionUID = 4889094948711185346L;

    @BsonId
    private String id;

    @BsonProperty("title")
    private String title;

    public Document mapToDocument() {
        Map<String, Object> docMap = new HashMap<>();

        docMap.put("id", id);
        docMap.put("title", title);

        return new Document(docMap);
    }
}
