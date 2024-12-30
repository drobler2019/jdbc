package org.example.configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.example.constant.UtilDataSource.*;

public class ConnectionDataSource {

    private static Connection connection;

    private ConnectionDataSource() {
    }

    public static Connection getSingleton() throws SQLException {
        if (connection == null) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return connection;
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL,USER,PASSWORD);
    }

    public static void closeConnection() throws SQLException {
        connection.close();
    }
}
