package org.example.service;

import org.example.entities.Category;
import org.example.entities.Product;

import java.sql.SQLException;
import java.util.List;

public interface Service {

    List<Product> findAll() throws SQLException;

    Product findById(Long id) throws SQLException;

    Product save(Product product) throws SQLException;

    void deleteById(Long id) throws SQLException;

    void saveProductWithCategory(Product product, Category category) throws SQLException;

    List<Category> findAllCategories() throws SQLException;

    Category findByIdCategory(Long id) throws SQLException;

    Category saveCategory(Category category) throws SQLException;

    void deleteCategory(Long id) throws SQLException;
}
