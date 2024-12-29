package org.example;

import org.example.repository.ProductDaoImpl;

public class Main {

    public static void main(String[] args) {
        var userDao = new ProductDaoImpl();
        userDao.findAllProducts().forEach(System.out::println);
    }

}