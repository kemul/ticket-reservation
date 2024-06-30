package com.sgedts.ticketreservation.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@Configuration
public class DataSourceConfig {

    @Value("${spring.datasource.url}")
    private String primaryDbUrl;

    @Value("${spring.datasource.username}")
    private String primaryDbUsername;

    @Value("${spring.datasource.password}")
    private String primaryDbPassword;

    @Value("${target.database.name}")
    private String dbName;

    @Value("${spring.profiles.active:default}")
    private String activeProfile;

    @PostConstruct
    private void init() {
        if ("local".equals(activeProfile) || "default".equals(activeProfile)) {
            try {
                createDatabaseIfNotExist();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void createDatabaseIfNotExist() throws SQLException {
        try (Connection connection = DriverManager.getConnection(primaryDbUrl, primaryDbUsername, primaryDbPassword);
                Statement statement = connection.createStatement()) {
            String createDbQuery = "CREATE DATABASE \"" + dbName + "\"";
            try {
                statement.executeUpdate(createDbQuery);
                System.out.println("Database created successfully");
            } catch (SQLException e) {
                if (e.getSQLState().equals("42P04")) {
                    System.out.println("Database already exists");
                } else {
                    throw e;
                }
            }
        }
    }
}
