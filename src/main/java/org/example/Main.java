package org.example;

import org.example.configuration.ConnectionDataSource;
import org.example.entities.Product;
import org.example.repository.ProductRepository;
import org.example.repository.impl.ProductRepositoryImpl;

import java.sql.SQLException;
import java.time.LocalDate;

public class Main {

    public static void main(String[] args) throws SQLException {
        var productRepository = new ProductRepositoryImpl();
        updateProduct(productRepository);
        ConnectionDataSource.closeConnection();
    }

    public static void listProducts(ProductRepository productRepository) {
        productRepository.findAll().forEach(System.out::println);
    }

    public static void findProduct(ProductRepository productRepository) {
        var optionalProduct = productRepository.findById(32L);
        optionalProduct.ifPresentOrElse(System.out::println, () -> System.out.println("usuario no encontrado"));
    }

    public static void saveProduct(ProductRepository productRepository) {
        var newProduct = new Product(null, "Secadora", 3000.0, LocalDate.now());
        productRepository.save(newProduct);
    }

    public static void updateProduct(ProductRepository productRepository) {
        var optionalProduct = productRepository.findById(11L);
        optionalProduct.ifPresentOrElse(product -> {
            var newProduct = new Product(product.id(), "Juego de niñas", 3000.0, null);
            productRepository.save(newProduct);
        }, () -> System.out.println("usuario no encontrado"));
    }

    public static void deleteProduct(ProductRepository productRepository) {
        productRepository.deleteById(12L);
    }

}