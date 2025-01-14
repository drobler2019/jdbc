package org.example.service;

import org.example.configuration.PoolDataSource;
import org.example.entities.Category;
import org.example.entities.Product;
import org.example.repository.CategoryRepository;
import org.example.repository.ProductRepository;
import org.example.repository.impl.CategoryRepositoryImpl;
import org.example.repository.impl.ProductRepositoryImpl;

import java.sql.SQLException;
import java.util.List;

public class ServiceImpl implements Service {

    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;

    public ServiceImpl() {
        this.productRepository = new ProductRepositoryImpl();
        this.categoryRepository = new CategoryRepositoryImpl();
    }

    @Override
    public List<Product> findAll() throws SQLException {
        try (var connection = PoolDataSource.getConnection()) {
            this.productRepository.setConnection(connection);
            return (List<Product>) this.productRepository.findAll();
        }
    }

    @Override
    public Product findById(Long id) throws SQLException {
        try (var connection = PoolDataSource.getConnection()) {
            this.productRepository.setConnection(connection);
            return this.productRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Product not found"));
        }
    }

    @Override
    public Product save(Product product) throws SQLException {
        try (var connection = PoolDataSource.getConnection()) {
            try {
                connection.setAutoCommit(false);
                this.productRepository.setConnection(connection);
                this.productRepository.save(product);
                connection.commit();
            } catch (RuntimeException e) {
                connection.rollback();
                e.printStackTrace();
            }
            return product;
        }
    }

    @Override
    public void deleteById(Long id) throws SQLException {
        try (var connection = PoolDataSource.getConnection()) {
            try {
                connection.setAutoCommit(false);
                this.productRepository.setConnection(connection);
                this.productRepository.deleteById(id);
                connection.commit();
            } catch (RuntimeException e) {
                connection.rollback();
                e.printStackTrace();
            }
        }
    }

    @Override
    public void saveProductWithCategory(Product product, Category category) throws SQLException {
        try (var connection = PoolDataSource.getConnection()) {
            connection.setAutoCommit(false);
            try {
                this.categoryRepository.setConnection(connection);
                this.productRepository.setConnection(connection);
                var categorynew = this.categoryRepository.save(category);
                product.setCategory(categorynew);
                this.productRepository.save(product);
                connection.commit();
            } catch (RuntimeException e) {
                connection.rollback();
                e.printStackTrace();
            }

        }
    }

    @Override
    public List<Category> findAllCategories() throws SQLException {
        try (var connection = PoolDataSource.getConnection()) {
            this.categoryRepository.setConnection(connection);
            return (List<Category>) this.categoryRepository.findAll();
        }
    }

    @Override
    public Category findByIdCategory(Long id) throws SQLException {
        try (var connection = PoolDataSource.getConnection()) {
            this.categoryRepository.setConnection(connection);
            return this.categoryRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Category not found"));
        }
    }

    @Override
    public Category saveCategory(Category category) throws SQLException {
        try (var connection = PoolDataSource.getConnection()) {
            try {
                connection.setAutoCommit(false);
                this.categoryRepository.setConnection(connection);
                this.categoryRepository.save(category);
                connection.commit();
            } catch (RuntimeException e) {
                connection.rollback();
                e.printStackTrace();
            }
            return category;
        }
    }

    @Override
    public void deleteCategory(Long id) throws SQLException {
        try (var connection = PoolDataSource.getConnection()) {
            try {
                connection.setAutoCommit(false);
                this.categoryRepository.setConnection(connection);
                this.productRepository.setConnection(connection);
                var products = this.productRepository.findProductWithCategory(id);
                products.stream().map(product ->  {
                    product.setCategory(null);
                    return product;
                }).forEach(this.productRepository::save);
                this.categoryRepository.deleteById(id);
                connection.commit();
            } catch (RuntimeException e) {
                connection.rollback();
                e.printStackTrace();
            }
        }
    }
}
