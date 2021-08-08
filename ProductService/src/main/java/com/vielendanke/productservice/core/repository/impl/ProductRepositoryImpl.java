package com.vielendanke.productservice.core.repository.impl;

import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;
import com.vielendanke.productservice.core.entity.Product;
import com.vielendanke.productservice.core.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class ProductRepositoryImpl implements ProductRepository {

    private final MongoDatabase shopDatabase;

    @Autowired
    public ProductRepositoryImpl(@Qualifier("shopDatabase") MongoDatabase shopDatabase) {
        this.shopDatabase = shopDatabase;
    }

    @Override
    public void updateQuantity(String id, int diffQuantity) {
        UpdateResult updateResult = shopDatabase.getCollection("products")
                .updateOne(new Document("id", id), new Document("inc", new Document("score", diffQuantity)));
        if (!updateResult.wasAcknowledged() || updateResult.getMatchedCount() < 1) {
            log.warn(String.format("Product with ID %s wasn't updated", id));
        }
    }

    @Override
    public String save(Product product) {
        Document doc = new Document(product.transformToDocument());
        shopDatabase.getCollection("products").insertOne(doc);
        return doc.get("id", String.class);
    }

    @Override
    public List<Product> findAll() {
        MongoCursor<Product> products = shopDatabase.getCollection("products", Product.class).find().cursor();
        List<Product> resProducts = new ArrayList<>();

        while (products.hasNext()) {
            resProducts.add(products.next());
        }
        return resProducts;
    }

    @Override
    public Product findById(String id) {
        return shopDatabase.getCollection("products").find(new Document("id", id), Product.class)
                .first();
    }

    @Override
    public Product findByIdOrTitle(String id, String title) {
        List<Document> documents = new ArrayList<>();
        documents.add(new Document("id", id));
        documents.add(new Document("title", title));
        return shopDatabase.getCollection("products").find(new Document("$or", documents), Product.class)
                .first();
    }
}
