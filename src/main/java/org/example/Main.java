package org.example;

import org.example.configuration.ConnectionDataSource;
import org.example.entities.Category;
import org.example.entities.Product;
import org.example.entities.User;
import org.example.repository.ProductRepository;
import org.example.repository.impl.UserRepositoryImpl;

import javax.swing.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws SQLException {

        final var userRepository = new UserRepositoryImpl();

        var option = 0;

        try (var connection = ConnectionDataSource.getSingleton()) {
            do {
                option = Integer.parseInt(JOptionPane.showInputDialog("elija una opción: \n1. Listar usuarios \n 2. Búscar usuario \n3. Registrar usuario \n4. Eliminar un usuario \n5. salir"));
                if (userRepository.findAll().isEmpty() && option != 3 && option != 5) {
                    JOptionPane.showConfirmDialog(null, "no hay usuarios", "Aceptar", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
                    continue;
                }
                switch (option) {
                    case 1 -> {
                        var users = userRepository.findAll().stream()
                                .map(user -> "id: " + user.id() + " " + "username: " + user.username() + " " + "email: " + user.email())
                                .collect(Collectors.joining("\n"));
                        message(users, "Lista de usuarios");
                    }
                    case 2 -> {
                        var id = Long.parseLong(JOptionPane.showInputDialog("digite id del usuario: "));
                        var optionalUser = userRepository.findById(id);
                        optionalUser.ifPresentOrElse(System.out::println, () -> message("usuario no encontrado", "Búsqueda de usuario"));
                    }
                    case 3 -> {
                        var username = JOptionPane.showInputDialog("digite username del usuario: ");
                        var password = JOptionPane.showInputDialog("digite password del usuario: ");
                        var email = JOptionPane.showInputDialog("digite email del usuario: ");
                        var user = new User(null, username, password, email);
                        userRepository.save(user);
                    }
                    case 4 -> {
                        var id = Long.parseLong(JOptionPane.showInputDialog("digite id del usuario: "));
                        var optionalUser = userRepository.findById(id);
                        optionalUser.ifPresentOrElse(user -> userRepository.deleteById(user.id()),
                                () -> message("usuario no encontrado", "Eliminar usuario"));
                    }
                }
            } while (option != 5);
        }


    }

    private static void message(String message, String title) {
        JOptionPane.showConfirmDialog(null, message, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
    }

    public static void listProducts(ProductRepository productRepository) {
        productRepository.findAll().forEach(System.out::println);
    }

    public static void findProduct(ProductRepository productRepository) {
        var optionalProduct = productRepository.findById(2L);
        optionalProduct.ifPresentOrElse(System.out::println, () -> System.out.println("usuario no encontrado"));
    }

    public static void saveProduct(ProductRepository productRepository) {
        var category = new Category(3L, null);
        var newProduct = new Product(null, "Secadora", 3000.0, category, LocalDate.now());
        productRepository.save(newProduct);
    }

    public static void updateProduct(ProductRepository productRepository) {
        var category = new Category(4L, null);
        var optionalProduct = productRepository.findById(11L);
        optionalProduct.ifPresentOrElse(product -> {
            var newProduct = new Product(product.id(), "Uvas", 3000.0, category, null);
            productRepository.save(newProduct);
        }, () -> System.out.println("usuario no encontrado"));
    }

    public static void deleteProduct(ProductRepository productRepository) {
        productRepository.deleteById(12L);
    }

}