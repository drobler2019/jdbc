package org.example.repository;

import org.example.entities.Product;

import java.util.List;

public interface ProductRepository extends Repository<Product,Long>,ConnectionRepository {
    List<Product> findProductWithCategory(Long id);
}
