package com.Illia.dao;


import com.Illia.config.DatabaseConfig;
import com.Illia.dto.ProfileDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProfileDAO {
    private static final String GET_PROFILE_QUERY = "SELECT * FROM profile WHERE email = ?";
    private static final String INSERT_PROFILE_QUERY = "INSERT INTO profile (email, address, phone, birth_day, birth_month, birth_year, first_name, last_name) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_PROFILE_QUERY = "UPDATE profile SET address = ?, phone = ?, birth_day = ?, birth_month = ?, birth_year = ?, first_name = ?, last_name = ? WHERE email = ?";
    private static final String DELETE_PROFILE_QUERY = "DELETE FROM profile WHERE email = ?";
    public ProfileDTO getProfileByEmail(Connection conn, String email) {
        try (PreparedStatement stmt = conn.prepareStatement(GET_PROFILE_QUERY)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            System.out.println("Trying to receive profile [" + email + "]");
            if (rs.next()) {
                return new ProfileDTO(
                    rs.getString("email"),
                    rs.getString("address"),
                    rs.getString("phone"),
                    rs.getInt("birth_day"),
                    rs.getInt("birth_month"),
                    rs.getInt("birth_year"),
                    rs.getString("first_name"),
                    rs.getString("last_name")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving profile data");
            e.printStackTrace();
            throw new RuntimeException("Error retrieving profile data", e);
        }
        return null;
    }
    public ProfileDTO getProfileByEmail(String email) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_PROFILE_QUERY)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            System.out.println("Trying recieve profile [" + email + "]");
            System.out.println(stmt.toString());
            if (rs.next()) {
                return new ProfileDTO(
                    rs.getString("email"),
                    rs.getString("address"),
                    rs.getString("phone"),
                    rs.getInt("birth_day"),
                    rs.getInt("birth_month"),
                    rs.getInt("birth_year"),
                    rs.getString("first_name"),
                    rs.getString("last_name")
                );
            }
            
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
        }catch (SQLException e) {
                    System.out.println("Error retrieving profile data");
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                    System.out.println(e.toString());
                    throw new RuntimeException("Error retrieving profile data", e);
                }
        return null;
    }

    public void createProfile(ProfileDTO profile) {
        System.out.println("creating profile in db");
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_PROFILE_QUERY)) {

            stmt.setString(1, profile.getEmail());
            stmt.setString(2, profile.getAddress());
            stmt.setString(3, profile.getPhone());
            stmt.setInt(4, profile.getBirthDay());
            stmt.setInt(5, profile.getBirthMonth());
            stmt.setInt(6, profile.getBirthYear());
            stmt.setString(7, profile.getFirstName());
            stmt.setString(8, profile.getLastName());

            stmt.executeUpdate();
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting profile data", e);
        }
    }

    public void updateProfile(ProfileDTO profile) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_PROFILE_QUERY)) {

            stmt.setString(1, profile.getAddress());
            stmt.setString(2, profile.getPhone());
            stmt.setInt(3, profile.getBirthDay());
            stmt.setInt(4, profile.getBirthMonth());
            stmt.setInt(5, profile.getBirthYear());
            stmt.setString(6, profile.getFirstName());
            stmt.setString(7, profile.getLastName());
            stmt.setString(8, profile.getEmail());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating profile data", e);
        }
    }

    public void deleteProfile(String email) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_PROFILE_QUERY)) {

            stmt.setString(1, email);
            stmt.executeUpdate();
    
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting profile data", e);
        }
    }
}
