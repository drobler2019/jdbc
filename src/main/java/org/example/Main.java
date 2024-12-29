package org.example;

import org.example.repository.impl.ProductRepositoryImpl;

public class Main {

    public static void main(String[] args) {
        var userDao = new ProductRepositoryImpl();
        userDao.findAll().forEach(System.out::println);
    }

}