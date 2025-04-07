package com.Illia.dao;

import com.Illia.dto.DispatcherDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DispatcherDAO {
    private static final String GET_UUID_QUERY = "SELECT id FROM dispatcher WHERE email = ?";

    public DispatcherDTO getDispatcherByEmail(Connection conn, String email) {
        try (PreparedStatement stmt = conn.prepareStatement(GET_UUID_QUERY)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            System.out.println("Trying to receive uuid for [" + email + "]");
            if (rs.next()) {
                return new DispatcherDTO(
                    email,
                    rs.getString("id")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving dispatcher data");
            throw new RuntimeException("Error retrieving dispatcher data", e);
        }
        return null;
    }

}
