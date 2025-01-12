package org.example.repository.impl;

import org.example.configuration.SingletonDataSource;
import org.example.constant.UtilDataSource;
import org.example.entities.Category;
import org.example.entities.Product;
import org.example.repository.ProductRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class ProductRepositoryImpl implements ProductRepository {

    private Connection connection;

    public ProductRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Collection<Product> findAll() {
        var products = new ArrayList<Product>();
        try (var stmt = this.connection.createStatement();
             var resultSet = stmt.executeQuery("SELECT prod.*,cat.nombre FROM productos as prod INNER JOIN categorias as cat ON (prod.categoria_id = cat.id)")) {
            while (resultSet.next()) {
                var product = this.getProduct(resultSet);
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
        try (var stmt = this.connection.prepareStatement("SELECT prod.*,cat.nombre FROM productos as prod INNER JOIN categorias as cat ON (prod.categoria_id = cat.id) WHERE prod.id = ?")) {
            stmt.setLong(1, id);
            try (var resultSet = stmt.executeQuery()) {
                while (resultSet.next()) {
                    product = this.getProduct(resultSet);
                }
                return Optional.ofNullable(product);
            }
        } catch (SQLException e) {
            this.logError(e);
            return Optional.empty();
        }
    }

    @Override
    public Product save(Product product) {
        String sql;
        if (UtilDataSource.hasId(product.id())) {
            sql = "UPDATE productos SET nombre=?, precio=?,categoria_id=?, sku=? WHERE id=?";
        } else {
            sql = "INSERT INTO productos(nombre,precio,categoria_id,sku,fecha_registro) VALUES (?,?,?,?,?)";
        }
        try (var stmt = this.connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, product.nombre());
            stmt.setDouble(2, product.precio());
            stmt.setLong(3, product.category().id());
            stmt.setString(4, product.sku());
            if (UtilDataSource.hasId(product.id())) {
                stmt.setLong(5, product.id());
            } else {
                stmt.setDate(5, Date.valueOf(product.fechaRegistro()));
            }
            stmt.executeUpdate();
            final var message = sql.contains("UPDATE") ? String.format("producto %s %s con éxito!", product.id(), "actualizado")
                    : "producto guardado con éxito!";

            var id = 0L;

            if (product.id() == null) {
                try (var resultSet = stmt.getGeneratedKeys()) {
                    id = resultSet.getLong(1);
                }
            }

            this.success(message);
            return new Product(id, product.nombre(), product.precio(), product.category(), product.fechaRegistro(), product.sku());
        } catch (SQLException e) {
            this.logError(e);
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void deleteById(Long id) {
        try (var stmt = this.connection.prepareStatement("DELETE FROM productos WHERE id=?")) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
            this.success("producto " + id + " eliminado con éxito");
        } catch (SQLException e) {
            this.logError(e);
        }
    }

    private Product getProduct(ResultSet resultSet) throws SQLException {
        var category = new Category(resultSet.getLong("categoria_id"), resultSet.getString("cat.nombre"));
        return new Product(resultSet.getLong("id"),
                resultSet.getString("nombre"),
                resultSet.getDouble("precio"),
                category,
                resultSet.getDate("fecha_registro").toLocalDate(), resultSet.getString("sku"));
    }

    private void success(String message) {
        System.out.println(message);
    }

    private void logError(SQLException e) {
        final var message = String.format("code: %s - detail: %s", e.getSQLState(), e.getMessage());
        System.err.println(message);
    }


}
