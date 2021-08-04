package com.vielendanke.productservice.core.repository;

import com.vielendanke.productservice.core.entity.Product;

public interface ProductRepository {

    String save(Product product);

    Product findById(String id);

    Product findByIdOrTitle(String id, String title);
}
