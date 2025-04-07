package com.Illia.dao;


import com.Illia.config.DatabaseConfig;
import com.Illia.dto.ProfileDTO;
import com.Illia.dto.QueryDTO;

import java.security.Timestamp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class QueryDAO {
    private static final String GET_QUERY_BY_EMAIL = "SELECT * FROM driver_query WHERE email = ?";
    private static final String GET_NEW_QUERY = "SELECT * FROM driver_query WHERE status='new'";
    private static final String GET_QUERY_BY_ID = "SELECT * FROM driver_query WHERE id = ? AND (status ='new' or status ='expired')";
    private static final String INSERT_QUERY = "INSERT INTO driver_query (add_datetime, start_datetime, email, start_location, destination, driver_experience, car_type, max_car_mileage, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"; 
    private static final String UPDATE_QUERY = "UPDATE driver_query SET add_datetime = ?, start_datetime = ?, email = ?, start_location = ?, destination = ?, driver_experience = ?, car_type = ?, max_car_mileage = ?, status = ? WHERE id = ?";
    private static final String DELETE_QUERY = "DELETE FROM driver_query WHERE id = ? AND (status ='new' or status ='expired')";
    private static final String UPDATE_QUERY_STATUS = "UPDATE driver_query SET status = ? WHERE id = ?";

    public ArrayList<QueryDTO> getNew(Connection conn) {
        try (PreparedStatement stmt = conn.prepareStatement(GET_NEW_QUERY)) {
            ResultSet rs = stmt.executeQuery();
            ArrayList<QueryDTO> queries = new ArrayList<>(); 
         while (rs.next()) {
            QueryDTO newDto = new QueryDTO(
                rs.getInt("id"),                          // id should be an integer
                rs.getTimestamp("add_datetime").toLocalDateTime(), // add_datetime as LocalDateTime
                rs.getTimestamp("start_datetime").toLocalDateTime(), // start_datetime as LocalDateTime
                rs.getString("email"),                    // email should be a string
                rs.getString("start_location"),           // start_location as string
                rs.getString("destination"),              // destination as string
                rs.getInt("driver_experience"),           // driver_experience as integer
                rs.getString("car_type"),                 // car_type as string
                rs.getInt("max_car_mileage"),             // max_car_mileage as integer
                rs.getString("status")             
            );
            System.out.println("recieved query: " + newDto.toString());
            queries.add(newDto);

            }
            return queries;
            } catch (SQLException e) {
            System.out.println("Error retrieving query data");
            e.printStackTrace();
            throw new RuntimeException("Error retrieving query data", e);
        }
        
    }
    public ArrayList<QueryDTO> getQueryByEmail(Connection conn, String email) {
        try (PreparedStatement stmt = conn.prepareStatement(GET_QUERY_BY_EMAIL)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            System.out.println("Trying to receive query [" + email + "]");
            ArrayList<QueryDTO> queries = new ArrayList<>(); 
         while (rs.next()) {
            QueryDTO newDto = new QueryDTO(
                rs.getInt("id"),                          // id should be an integer
                rs.getTimestamp("add_datetime").toLocalDateTime(), // add_datetime as LocalDateTime
                rs.getTimestamp("start_datetime").toLocalDateTime(), // start_datetime as LocalDateTime
                rs.getString("email"),                    // email should be a string
                rs.getString("start_location"),           // start_location as string
                rs.getString("destination"),              // destination as string
                rs.getInt("driver_experience"),           // driver_experience as integer
                rs.getString("car_type"),                 // car_type as string
                rs.getInt("max_car_mileage"),             // max_car_mileage as integer
                rs.getString("status")             // is_satisfied as boolean
            );
            System.out.println("recieved query: " + newDto.toString());
            queries.add(newDto);

            }
            return queries;
            } catch (SQLException e) {
            System.out.println("Error retrieving query data");
            e.printStackTrace();
            throw new RuntimeException("Error retrieving query data", e);
        }
        
    }
    public QueryDTO getQueryByID(Connection conn, int id) {
        try (PreparedStatement stmt = conn.prepareStatement(GET_QUERY_BY_ID)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            System.out.println("Trying to receive query [" + id + "]");
         if (rs.next()) {
            return (new QueryDTO(
                rs.getInt("id"),                          // id should be an integer
                rs.getTimestamp("add_datetime").toLocalDateTime(), // add_datetime as LocalDateTime
                rs.getTimestamp("start_datetime").toLocalDateTime(), // start_datetime as LocalDateTime
                rs.getString("email"),                    // email should be a string
                rs.getString("start_location"),           // start_location as string
                rs.getString("destination"),              // destination as string
                rs.getInt("driver_experience"),           // driver_experience as integer
                rs.getString("car_type"),                 // car_type as string
                rs.getInt("max_car_mileage"),             // max_car_mileage as integer
                rs.getString("status")             // is_satisfied as boolean
            ));
                }
            } catch (SQLException e) {
            System.out.println("Error retrieving query data");
            e.printStackTrace();
            throw new RuntimeException("Error retrieving query data", e);
        }
        return null;
    }
   
    public void createQuery(QueryDTO query) {
        System.out.println("Creating query in database");
       
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_QUERY)) {
            stmt.setTimestamp(1,  java.sql.Timestamp.valueOf(query.getAddDatetime()));
            stmt.setTimestamp(2, java.sql.Timestamp.valueOf(query.getStartDatetime()));
            stmt.setString(3, query.getEmail());
            stmt.setString(4, query.getStartLocation());
            stmt.setString(5, query.getDestination());
            stmt.setInt(6, query.getDriverExperience());
            stmt.setString(7, query.getCarType());
            stmt.setInt(8, query.getMaxCarMileage());
            stmt.setString(9, query.getStatus());
    
            stmt.executeUpdate(); // Execute the query
    
        } catch (SQLException e) {
            System.err.println("Error inserting query data: " + e.getMessage());
            throw new RuntimeException("Error inserting query data", e);
        }
    }
    public void updateQuery(QueryDTO query) {
        System.out.println("Creating query in database");
       
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_QUERY)) {
            stmt.setTimestamp(1,  java.sql.Timestamp.valueOf(query.getAddDatetime()));
            stmt.setTimestamp(2, java.sql.Timestamp.valueOf(query.getStartDatetime()));
            stmt.setString(3, query.getEmail());
            stmt.setString(4, query.getStartLocation());
            stmt.setString(5, query.getDestination());
            stmt.setInt(6, query.getDriverExperience());
            stmt.setString(7, query.getCarType());
            stmt.setInt(8, query.getMaxCarMileage());
            stmt.setString(9, query.getStatus());
            stmt.setInt(10, query.getId());
            stmt.executeUpdate(); // Execute the query
    
        } catch (SQLException e) {
            System.err.println("Error inserting query data: " + e.getMessage());
            throw new RuntimeException("Error inserting query data", e);
        }
    }
    public void deleteQuery(int id) {
        System.out.println("Delete query " + id +  " in database");
       
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_QUERY)) {
            stmt.setInt(1, id);
    
            stmt.executeUpdate(); // Execute the query
    
        } catch (SQLException e) {
            System.err.println("Error inserting query data: " + e.getMessage());
            throw new RuntimeException("Error inserting query data", e);
        }
    }
    public void updateQueryStatus(int queryId, String new_status) {
        System.out.println("Creating query in database");
       
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_QUERY_STATUS)) {
            stmt.setString(1, new_status);
            stmt.setInt(2, queryId);
            stmt.executeUpdate(); // Execute the query
    
        } catch (SQLException e) {
            System.err.println("Error inserting query data: " + e.getMessage());
            throw new RuntimeException("Error inserting query data", e);
        }
    }
}
