package com.Illia.service;

import com.Illia.config.DatabaseConfig;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseTableHandler {

    public void processRequest(HttpServletRequest request) {
        List<String> tableNames = new ArrayList<>();
        try (Connection connection = DatabaseConfig.getConnection()) { // Используем DatabaseConfig для получения соединения
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet tables = metaData.getTables(null, null, "%", new String[]{"TABLE"});

            while (tables.next()) {
                tableNames.add(tables.getString("TABLE_NAME"));
            }

            request.setAttribute("tableNames", tableNames); // Сохраняем список таблиц в атрибуте запроса
        } catch (SQLException e) {
            e.printStackTrace(); // Обрабатываем исключения
        }
    }
}