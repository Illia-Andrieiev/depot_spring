package com.Illia.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
//import java.util.Properties;

public class DatabaseConfig {
    private static final String URL = "jdbc:postgresql://localhost:5432/depot";
    private static final String USER = "postgres";
    private static final String PASSWORD = "Aa2324252627*gh34//*asa+1";

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("PostgreSQL Driver not found!", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
