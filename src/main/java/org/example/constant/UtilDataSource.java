package org.example.constant;

public class UtilDataSource {

    public static final String URL = "jdbc:mysql://localhost:3306/jdbc?serverTimeZone=UTC";
    public static final String USER = "root";
    public static final String PASSWORD = "12345";
    public static final int INITIAL_CONNECTION_ACTIVE_SIZE = 3;
    public static final int MAX_CONNECTION_ACTIVE = 8;
    public static final int MIN_CONNECTION_INACTIVE = 3;
    public static final int MAX_CONNECTION_TOTAL = 8;

    public static boolean hasId(Long id) {
        return id != null && id > 0;
    }

}
