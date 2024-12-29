package org.example.repository;

import org.example.configuration.ConnectionDataSource;
import org.example.entities.Product;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDaoImpl implements ProductDao {

    @Override
    public List<Product> findAllProducts() {
        var products = new ArrayList<Product>();
        try (var connection = ConnectionDataSource.getConnection();
             var stmt = connection.createStatement();
             var resultSet = stmt.executeQuery("SELECT * FROM productos")) {

            while (resultSet.next()) {
                var product = new Product(resultSet.getLong("id"),
                        resultSet.getString("nombre"),
                        resultSet.getDouble("precio"),
                        resultSet.getDate("fecha_registro"));
                products.add(product);
            }

            return products;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
