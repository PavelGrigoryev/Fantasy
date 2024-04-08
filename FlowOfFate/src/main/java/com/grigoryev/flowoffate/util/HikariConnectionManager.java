package com.grigoryev.flowoffate.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
@UtilityClass
public class HikariConnectionManager {

    private final HikariConfig CONFIG = new HikariConfig();
    private final HikariDataSource DATA_SOURCE;

    static {
        CONFIG.setJdbcUrl("jdbc:postgresql://localhost:5432/fantasy");
        CONFIG.setUsername("pavel");
        CONFIG.setPassword("pavel");
        CONFIG.setDriverClassName("org.postgresql.Driver");
        CONFIG.setMaximumPoolSize(30);
        DATA_SOURCE = new HikariDataSource(CONFIG);
    }

    public Connection getConnection() {
        Connection connection;
        try {
            connection = DATA_SOURCE.getConnection();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException();
        }
        return connection;
    }

}
