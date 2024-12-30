package org.example.repository.impl;

import org.example.configuration.ConnectionDataSource;
import org.example.entities.Category;
import org.example.entities.Product;
import org.example.repository.ProductRepository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
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
        try (var stmt = this.getConnection().prepareStatement("SELECT prod.*,cat.nombre FROM productos as prod INNER JOIN categorias as cat ON (prod.categoria_id = cat.id) WHERE prod.id = ?")) {
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
    public void save(Product product) {
        String sql;
        if (this.hasId(product.id())) {
            sql = "UPDATE productos SET nombre=?, precio=?,categoria_id=? WHERE id=?";
        } else {
            sql = "INSERT INTO productos(nombre,precio,categoria_id,fecha_registro) VALUES (?,?,?,?)";
        }
        try (var stmt = this.getConnection().prepareStatement(sql)) {
            stmt.setString(1, product.nombre());
            stmt.setDouble(2, product.precio());
            stmt.setLong(3, product.category().id());
            if (this.hasId(product.id())) {
                stmt.setLong(4, product.id());
            } else {
                stmt.setDate(4, Date.valueOf(product.fechaRegistro()));
            }
            stmt.executeUpdate();
            final var message = sql.contains("UPDATE") ? String.format("producto %s %s con éxito!", product.id(), "actualizado")
                    : "producto guardado con éxito!";
            this.success(message);
        } catch (SQLException e) {
            this.logError(e);
        }
    }

    @Override
    public void deleteById(Long id) {
        try (var stmt = this.getConnection().prepareStatement("DELETE FROM productos WHERE id=?")) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
            this.success("producto " + id + " eliminado con éxito");
        } catch (SQLException e) {
            this.logError(e);
        }
    }

    private boolean hasId(Long id) {
        return id != null && id > 0;
    }

    private Product getProduct(ResultSet resultSet) throws SQLException {
        var category = new Category(resultSet.getLong("categoria_id"), resultSet.getString("cat.nombre"));
        return new Product(resultSet.getLong("id"),
                resultSet.getString("nombre"),
                resultSet.getDouble("precio"),
                category,
                resultSet.getDate("fecha_registro").toLocalDate());
    }

    private void success(String message) {
        System.out.println(message);
    }

    private void logError(SQLException e) {
        final var message = String.format("code: %s - detail: %s", e.getSQLState(), e.getMessage());
        System.err.println(message);
    }


}
