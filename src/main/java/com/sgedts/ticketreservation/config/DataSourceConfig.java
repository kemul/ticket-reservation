package com.sgedts.ticketreservation.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
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

    @Value("${spring.datasource.driver-class-name}")
    private String primaryDbDriverClassName;

    @PostConstruct
    public void init() throws SQLException {
        createDatabaseIfNotExist();
    }

    private void createDatabaseIfNotExist() throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/postgres";
        String dbName = "db-ticket-reservation";
        try (Connection connection = DriverManager.getConnection(url, primaryDbUsername, primaryDbPassword);
                Statement statement = connection.createStatement()) {
            String createDbQuery = "CREATE DATABASE \"" + dbName + "\"";
            try {
                statement.executeUpdate(createDbQuery);
                System.out.println("Database created successfully");
            } catch (SQLException e) {
                if (e.getSQLState().equals("42P04")) { // "42P04" is the SQLState code for "database already exists"
                    System.out.println("Database already exists");
                } else {
                    throw e;
                }
            }
        }
    }

    @Bean
    public DataSource dataSource() {
        return org.springframework.boot.jdbc.DataSourceBuilder.create()
                .url(primaryDbUrl)
                .username(primaryDbUsername)
                .password(primaryDbPassword)
                .driverClassName(primaryDbDriverClassName)
                .build();
    }
}
