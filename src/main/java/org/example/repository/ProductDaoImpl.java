package org.example.repository;

import org.example.configuration.ConnectionDataSource;

import java.sql.SQLException;

public class ProductDaoImpl implements ProductDao {

    @Override
    public void obtenerProductos() {
        try (var connection = ConnectionDataSource.getConnection();
             var stmt = connection.createStatement();
             var resultSet = stmt.executeQuery("SELECT * FROM productos")) {
            while (resultSet.next()) {
                System.out.println(resultSet.getString("nombre"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
