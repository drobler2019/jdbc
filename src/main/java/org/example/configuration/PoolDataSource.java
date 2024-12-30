package org.example.configuration;

import org.apache.commons.dbcp2.BasicDataSource;

import static org.example.constant.UtilDataSource.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class PoolDataSource {


    private static BasicDataSource dataSource;

    private PoolDataSource() {
    }

    public static DataSource getPoolConfiguration() {
        if (dataSource == null) {
            dataSource = new BasicDataSource();
            dataSource.setUrl(URL);
            dataSource.setUsername(USER);
            dataSource.setPassword(PASSWORD);
            dataSource.setInitialSize(INITIAL_CONNECTION_ACTIVE_SIZE);
            dataSource.setMinIdle(MIN_CONNECTION_INACTIVE);
            dataSource.setMaxIdle(MAX_CONNECTION_ACTIVE);
            dataSource.setMaxTotal(MAX_CONNECTION_TOTAL);
        }
        return dataSource;
    }

    public static Connection getConnection() throws SQLException {
        return getPoolConfiguration().getConnection();
    }

}
