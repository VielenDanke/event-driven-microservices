package com.vielendanke.productservice.core.repository.impl;

import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.vielendanke.productservice.core.entity.Product;
import com.vielendanke.productservice.core.repository.ProductRepository;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    private final MongoDatabase shopDatabase;

    @Autowired
    public ProductRepositoryImpl(@Qualifier("shopDatabase") MongoDatabase shopDatabase) {
        this.shopDatabase = shopDatabase;
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
        Document[] toFind = new Document[2];
        toFind[0] = new Document("id", id);
        toFind[1] = new Document("title", title);
        return shopDatabase.getCollection("products").find(new Document("or", toFind), Product.class)
                .first();
    }
}
