package org.example.repository.impl;

import org.example.configuration.ConnectionDataSource;
import org.example.entities.Product;
import org.example.repository.ProductRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class ProductRepositoryImpl implements ProductRepository {

    public Connection getConnection() throws SQLException {
        return ConnectionDataSource.getConnection();
    }

    @Override
    public Collection<Product> findAll() {
        var products = new ArrayList<Product>();
        try (var stmt = this.getConnection().createStatement();
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
        Product product = null;
        try (var stmt = this.getConnection().prepareStatement("SELECT * FROM productos WHERE id = ?")) {

            stmt.setLong(1, id);

            try (var resultSet = stmt.executeQuery()) {
                while (resultSet.next()) {
                    product = new Product(resultSet.getLong("id"),
                            resultSet.getString("nombre"),
                            resultSet.getDouble("precio"),
                            resultSet.getDate("fecha_registro").toLocalDate());
                }

                return Optional.ofNullable(product);
            }

        } catch (SQLException e) {
            final var message = String.format("code: %s - detail: %s", e.getSQLState(), e.getMessage());
            System.err.println(message);
            return Optional.empty();
        }
    }

    @Override
    public void save(Product product) {

    }

    @Override
    public void deleteById(Long id) {

    }

}
