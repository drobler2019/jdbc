package org.example;

import org.example.configuration.ConnectionDataSource;
import org.example.repository.impl.ProductRepositoryImpl;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException {
        var productRepository = new ProductRepositoryImpl();
        productRepository.findAll().forEach(System.out::println);
        var optionalProduct = productRepository.findById(32L);
        optionalProduct.ifPresentOrElse(System.out::println, () -> System.out.println("usuario no encontrado"));
        ConnectionDataSource.closeConnection();
    }

}