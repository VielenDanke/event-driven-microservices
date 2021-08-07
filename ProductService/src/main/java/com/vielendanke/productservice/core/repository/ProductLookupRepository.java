package com.vielendanke.productservice.core.repository;

import com.vielendanke.productservice.core.model.ProductLookupEntity;

public interface ProductLookupRepository {

    boolean checkIfExists(String id, String title);

    void save(ProductLookupEntity productLookupEntity);
}
