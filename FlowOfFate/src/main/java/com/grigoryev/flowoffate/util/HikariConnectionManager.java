package com.grigoryev.flowoffate.util;

import com.grigoryev.flowoffate.exception.HikariConnectionPoolException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

@Slf4j
@UtilityClass
public class HikariConnectionManager {

    private final HikariConfig CONFIG = new HikariConfig();
    private final HikariDataSource DATA_SOURCE;

    static {
        Map<String, String> postgresql = YamlUtil.getYaml().get("postgresql");
        CONFIG.setJdbcUrl(postgresql.get("url"));
        CONFIG.setUsername(postgresql.get("user"));
        CONFIG.setPassword(postgresql.get("password"));
        CONFIG.setDriverClassName(postgresql.get("driver"));
        CONFIG.setMaximumPoolSize(Integer.parseInt(postgresql.get("maximumPoolSize")));
        DATA_SOURCE = new HikariDataSource(CONFIG);
    }

    public Connection getConnection() {
        Connection connection;
        try {
            connection = DATA_SOURCE.getConnection();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new HikariConnectionPoolException("Hikari connection pool error %s".formatted(e.getMessage()));
        }
        return connection;
    }

}
