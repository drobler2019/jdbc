package org.example.repository.impl;

import org.example.configuration.ConnectionDataSource;
import org.example.entities.Product;
import org.example.repository.ProductRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class ProductRepositoryImpl implements ProductRepository {

    @Override
    public Collection<Product> findAll() {
        var products = new ArrayList<Product>();
        try (var connection = ConnectionDataSource.getConnection();
             var stmt = connection.createStatement();
             var resultSet = stmt.executeQuery("SELECT * FROM productos")) {

            while (resultSet.next()) {
                var product = new Product(resultSet.getLong("id"),
                        resultSet.getString("nombre"),
                        resultSet.getDouble("precio"),
                        resultSet.getDate("fecha_registro").toLocalDate());
                products.add(product);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return products;
    }

    @Override
    public Optional<Product> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public void save(Product product) {

    }

}
