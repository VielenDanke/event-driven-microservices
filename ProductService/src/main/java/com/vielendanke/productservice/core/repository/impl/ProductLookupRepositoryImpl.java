package com.vielendanke.productservice.core.repository.impl;

import com.mongodb.client.MongoDatabase;
import com.vielendanke.productservice.core.model.ProductLookupEntity;
import com.vielendanke.productservice.core.repository.ProductLookupRepository;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ProductLookupRepositoryImpl implements ProductLookupRepository {

    private final MongoDatabase shopDatabase;

    @Autowired
    public ProductLookupRepositoryImpl(MongoDatabase shopDatabase) {
        this.shopDatabase = shopDatabase;
    }

    @Override
    public boolean checkIfExists(String id, String title) {
        Document[] documents = new Document[2];
        documents[0] = new Document("id", id);
        documents[1] = new Document("title", title);
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
