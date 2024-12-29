package org.example;

import org.example.repository.ProductDao;
import org.example.repository.ProductDaoImpl;

public class Main {

    public static void main(String[] args) {
        ProductDao userDao = new ProductDaoImpl();
        userDao.obtenerProductos();
    }

}