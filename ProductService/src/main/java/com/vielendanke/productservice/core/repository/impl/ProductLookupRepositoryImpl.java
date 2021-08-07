package com.vielendanke.productservice.core.repository.impl;

import com.mongodb.client.MongoDatabase;
import com.vielendanke.productservice.core.model.ProductLookupEntity;
import com.vielendanke.productservice.core.repository.ProductLookupRepository;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductLookupRepositoryImpl implements ProductLookupRepository {

    private final MongoDatabase shopDatabase;

    @Autowired
    public ProductLookupRepositoryImpl(MongoDatabase shopDatabase) {
        this.shopDatabase = shopDatabase;
    }

    @Override
    public boolean checkIfExists(String id, String title) {
        List<Document> documents = new ArrayList<>();
        documents.add(new Document("id", id));
        documents.add(new Document("title", title));
        Document toFind = new Document("or", documents);
        return shopDatabase.getCollection("productsLookup")
                .find(toFind, ProductLookupEntity.class)
                .cursor().hasNext();
    }

    @Override
    public void save(ProductLookupEntity productLookupEntity) {
        shopDatabase.getCollection("productsLookup")
                .insertOne(productLookupEntity.mapToDocument());
    }
}
