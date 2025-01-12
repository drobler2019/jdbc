package org.example;

import org.example.configuration.PoolDataSource;
import org.example.configuration.SingletonDataSource;
import org.example.entities.Category;
import org.example.entities.Product;
import org.example.entities.User;
import org.example.repository.CategoryRepository;
import org.example.repository.ProductRepository;
import org.example.repository.impl.CategoryRepositoryImpl;
import org.example.repository.impl.ProductRepositoryImpl;
import org.example.repository.impl.UserRepositoryImpl;

import javax.swing.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        transactionExample();
    }

    private static void transactionExample() {
        try (var connection = PoolDataSource.getConnection()) {
            connection.setAutoCommit(false);
            try {

                var categoryRepository = new CategoryRepositoryImpl(connection);
                var productRepository = new ProductRepositoryImpl(connection);
                //transacción
                saveProduct(productRepository, categoryRepository);
                updateProduct(productRepository);
                connection.commit();

            } catch (RuntimeException e) {
                connection.rollback();
                System.out.println(e.getMessage());
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void crudUser() {
        final var userRepository = new UserRepositoryImpl();
        var option = 0;
        do {
            option = Integer.parseInt(JOptionPane.showInputDialog("elija una opción: \n1. Listar usuarios \n 2. Búscar usuario \n3. Registrar usuario \n4.Actualizar usuario \n5. Eliminar un usuario \n6. salir"));
            if (userRepository.findAll().isEmpty() && option != 3 && option != 5) {
                JOptionPane.showConfirmDialog(null, "no hay usuarios", "Aceptar", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
                continue;
            }
            switch (option) {
                case 1 -> {
                    var users = getUsers(userRepository);
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
                    var users = getUsers(userRepository);
                    var id = Long.parseLong(JOptionPane.showInputDialog(users + "\ndigite id de usuario a actualizar"));
                    var user = userRepository.findById(id).orElseThrow();
                    message(user.toString(), "Información Usuario a actualizar");
                    var username = JOptionPane.showInputDialog("nuevo username:");
                    var password = JOptionPane.showInputDialog("nueva password:");
                    var email = JOptionPane.showInputDialog("nuevo email:");
                    var newUser = new User(user.id(), username, password, email);
                    userRepository.save(newUser);
                }
                case 5 -> {
                    var id = Long.parseLong(JOptionPane.showInputDialog("digite id del usuario: "));
                    var optionalUser = userRepository.findById(id);
                    optionalUser.ifPresentOrElse(user -> userRepository.deleteById(user.id()),
                            () -> message("usuario no encontrado", "Eliminar usuario"));
                }
            }
        } while (option != 6);
    }

    private static String getUsers(UserRepositoryImpl userRepository) {
        return userRepository.findAll().stream()
                .map(user -> "id: " + user.id() + " " + "username: " + user.username() + " " + "email: " + user.email())
                .collect(Collectors.joining("\n"));
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

    public static void saveProduct(ProductRepository productRepository, CategoryRepository categoryRepository) {
        var category = new Category(null, "TECHNOLOGY");
        var categorySave = categoryRepository.save(category);
        var newProduct = new Product(null, "Lenovo nitro 5", 3000.0, categorySave, LocalDate.now(), "abcde12345");
        productRepository.save(newProduct);
    }

    public static void updateProduct(ProductRepository productRepository) {
        var category = new Category(4L, null);
        var optionalProduct = productRepository.findById(1L);
        optionalProduct.ifPresentOrElse(product -> {
            var newProduct = new Product(product.id(), "MotherBoard", 30000.0, category, null, "12345abcd");
            productRepository.save(newProduct);
        }, () -> System.out.println("usuario no encontrado"));
    }

    public static void deleteProduct(ProductRepository productRepository) {
        productRepository.deleteById(12L);
    }

}