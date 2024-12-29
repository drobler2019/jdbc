package org.example.configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDataSource {

    private static final String URL = "jdbc:mysql://localhost:3306/jdbc?serverTimeZone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "12345";

    private static Connection connection;

    private ConnectionDataSource() {
    }

    public static Connection getConnection() throws SQLException {
        if (connection == null) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return connection;
    }
}
