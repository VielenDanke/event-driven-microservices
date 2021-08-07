package com.vielendanke.productservice.core.repository;

import com.vielendanke.productservice.core.entity.Product;

import java.util.List;

public interface ProductRepository {

    String save(Product product);

    List<Product> findAll();

    Product findById(String id);

    Product findByIdOrTitle(String id, String title);

    void updateQuantity(String id, int diffQuantity);
}
