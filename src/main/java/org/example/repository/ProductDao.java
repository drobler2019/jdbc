package org.example.repository;

import org.example.entities.Product;

import java.util.List;

public interface ProductDao {

    List<Product> findAllProducts();

}
