package com.Illia.dao;

import com.Illia.config.DatabaseConfig;
import com.Illia.dto.DriverCarDTO;
import com.Illia.dto.QueryDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DriverCarDAO {
    private static final String GET_PROFILE_QUERY = "SELECT * FROM driver JOIN car ON car.vin = driver.car_vin WHERE driver.email = ?";

    public DriverCarDTO getProfileByEmail(Connection conn, String email) {
        try (PreparedStatement stmt = conn.prepareStatement(GET_PROFILE_QUERY)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            System.out.println("Trying to receive profile [" + email + "]");
            if (rs.next()) {
                DriverCarDTO res = new DriverCarDTO();
                res.setId(rs.getInt("id"));
                res.setEmail(rs.getString("email"));
                res.setAdditionalInformation(rs.getString("additional_information"));
                res.setAge(rs.getInt("age"));
                res.setBrand(rs.getString("brand"));
                res.setDriverLicenceType(rs.getString("driver_licence_type"));
                res.setExperience(rs.getInt("experience"));
                res.setFuelType(rs.getString("fuel_type"));
                res.setMileage(rs.getInt("mileage"));
                res.setNumber(rs.getString("number"));
                res.setServiceable(rs.getBoolean("is_serviceable"));
                res.setType(rs.getString("type"));
                res.setVin(rs.getString("vin"));
                res.setYearOfManufacture(rs.getInt("year_of_manufacture"));
                return res;
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving profile data");
            e.printStackTrace();
            throw new RuntimeException("Error retrieving profile data", e);
        }
        return null;
    }
    public DriverCarDTO getProfileByEmail(String email) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_PROFILE_QUERY)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            System.out.println("Trying recieve profile [" + email + "]");
            System.out.println(stmt.toString());
            if (rs.next()) {
                DriverCarDTO res = new DriverCarDTO();
                res.setId(rs.getInt("id"));
                res.setEmail(rs.getString("email"));
                res.setAdditionalInformation(rs.getString("additional_information"));
                res.setAge(rs.getInt("age"));
                res.setBrand(rs.getString("brand"));
                res.setDriverLicenceType(rs.getString("driver_licence_type"));
                res.setExperience(rs.getInt("experience"));
                res.setFuelType(rs.getString("fuel_type"));
                res.setMileage(rs.getInt("mileage"));
                res.setNumber(rs.getString("number"));
                res.setServiceable(rs.getBoolean("is_serviceable"));
                res.setType(rs.getString("type"));
                res.setVin(rs.getString("vin"));
                res.setYearOfManufacture(rs.getInt("year_of_manufacture"));
                return res;
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

 public ArrayList<DriverCarDTO> getAvailableByQuery(int id) {
    try (Connection conn = DatabaseConfig.getConnection()) {
        // query
        String querySql = "SELECT * FROM driver_query WHERE id = ?";
        QueryDTO query;
        try (PreparedStatement queryStmt = conn.prepareStatement(querySql)) {
            queryStmt.setInt(1, id);
            ResultSet queryRs = queryStmt.executeQuery();
            if (!queryRs.next()) {
                System.out.println("No query found with ID: " + id);
                return new ArrayList<>();
            }

            query = new QueryDTO(
                queryRs.getInt("id"),
                queryRs.getTimestamp("add_datetime").toLocalDateTime(),
                queryRs.getTimestamp("start_datetime").toLocalDateTime(),
                queryRs.getString("email"),
                queryRs.getString("start_location"),
                queryRs.getString("destination"),
                queryRs.getInt("driver_experience"),
                queryRs.getString("car_type"),
                queryRs.getInt("max_car_mileage"),
                queryRs.getString("status")
            );
        }

        // drivers
        String matchSql = """
            SELECT d.id AS driver_id, d.email, d.additional_information, d.age, d.driver_licence_type, 
                   d.experience, c.vin, c.brand, c.type, c.mileage, c.fuel_type, 
                   c.year_of_manufacture, c.is_serviceable, c.number
            FROM driver d
            JOIN car c ON c.vin = d.car_vin
            WHERE d.experience >= ?
              AND c.type = ?
              AND c.mileage <= ?
              AND c.is_serviceable = true
        """;

        ArrayList<DriverCarDTO> result = new ArrayList<>();

        try (PreparedStatement matchStmt = conn.prepareStatement(matchSql)) {
            matchStmt.setInt(1, query.getDriverExperience());
            matchStmt.setString(2, query.getCarType());
            matchStmt.setInt(3, query.getMaxCarMileage());

            ResultSet rs = matchStmt.executeQuery();
            while (rs.next()) {
                DriverCarDTO res = new DriverCarDTO();
                res.setId(rs.getInt("driver_id"));
                res.setEmail(rs.getString("email"));
                res.setAdditionalInformation(rs.getString("additional_information"));
                res.setAge(rs.getInt("age"));
                res.setBrand(rs.getString("brand"));
                res.setDriverLicenceType(rs.getString("driver_licence_type"));
                res.setExperience(rs.getInt("experience"));
                res.setFuelType(rs.getString("fuel_type"));
                res.setMileage(rs.getInt("mileage"));
                res.setNumber(rs.getString("number"));
                res.setServiceable(rs.getBoolean("is_serviceable"));
                res.setType(rs.getString("type"));
                res.setVin(rs.getString("vin"));
                res.setYearOfManufacture(rs.getInt("year_of_manufacture"));
                result.add(res);
            }
        }

        return result;
    } catch (SQLException e) {
        System.out.println("Error retrieving profile data");
        e.printStackTrace();
        throw new RuntimeException("Error retrieving profile data", e);
    }
}


    public void createProfile(DriverCarDTO profile) {
        System.out.println("creating profile in db");
        System.out.println(profile.toString());
        try (Connection conn = DatabaseConfig.getConnection();) {
                try {
                    // Insert statement for the 'driver' table
                    String insertDriver = "INSERT INTO driver (email, car_vin, age, experience, driver_licence_type, additional_information) VALUES (?, ?, ?, ?, ?, ?)";
                    PreparedStatement driverStmt = conn.prepareStatement(insertDriver);
                    driverStmt.setString(1, profile.getEmail()); // Example email
                    driverStmt.setString(2, profile.getVin()); // Corresponding car VIN
                    driverStmt.setInt(3, profile.getAge()); // Age
                    driverStmt.setInt(4, profile.getExperience()); // Years of experience
                    driverStmt.setString(5, profile.getDriverLicenceType()); // Driver license type
                    driverStmt.setString(6, profile.getAdditionalInformation()); // Any additional information
                    driverStmt.executeUpdate();
                    // Insert statement for the 'car' table
                    String deleteCar = "DELETE FROM car WHERE vin = ?";
                    PreparedStatement delcarStmt = conn.prepareStatement(deleteCar);
                    delcarStmt.setString(1, profile.getVin()); 
                    String insertCar = "INSERT INTO car (vin, brand, year_of_manufacture, type, fuel_type, mileage, number, is_serviceable) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement carStmt = conn.prepareStatement(insertCar);
                    carStmt.setString(1, profile.getVin()); // Example VIN
                    carStmt.setString(2, profile.getBrand());
                    carStmt.setInt(3, profile.getYearOfManufacture()); // Year of manufacture
                    carStmt.setString(4, profile.getType());
                    carStmt.setString(5, profile.getFuelType());
                    carStmt.setInt(6, profile.getMileage()); // Mileage
                    carStmt.setString(7, profile.getNumber()); // Example number
                    carStmt.setBoolean(8, profile.isServiceable()); // Is serviceable
                    carStmt.executeUpdate();
        
     
        
                    System.out.println("Data successfully inserted!");
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (conn != null) {
                            conn.close();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting profile data", e);
        }
    }

    public void updateProfile(DriverCarDTO profile) {
        System.out.println("updating profile in db");
        try (Connection conn = DatabaseConfig.getConnection();) {
            try {
                // Update statement for the 'car' table
                String updateCar = "UPDATE car SET brand = ?, year_of_manufacture = ?, type = ?, fuel_type = ?, mileage = ?, number = ?, is_serviceable = ? WHERE vin = ?";
                PreparedStatement carStmt = conn.prepareStatement(updateCar);
                carStmt.setString(1, profile.getBrand());
                carStmt.setInt(2, profile.getYearOfManufacture());
                carStmt.setString(3, profile.getType());
                carStmt.setString(4, profile.getFuelType());
                carStmt.setInt(5, profile.getMileage());
                carStmt.setString(6, profile.getNumber());
                carStmt.setBoolean(7, profile.isServiceable());
                carStmt.setString(8, profile.getVin()); // Update based on VIN
                carStmt.executeUpdate();
    
                // Update statement for the 'driver' table
                String updateDriver = "UPDATE driver SET email = ?, age = ?, experience = ?, driver_licence_type = ?, additional_information = ? WHERE car_vin = ?";
                PreparedStatement driverStmt = conn.prepareStatement(updateDriver);
                driverStmt.setString(1, profile.getEmail());
                driverStmt.setInt(2, profile.getAge());
                driverStmt.setInt(3, profile.getExperience());
                driverStmt.setString(4, profile.getDriverLicenceType());
                driverStmt.setString(5, profile.getAdditionalInformation());
                driverStmt.setString(6, profile.getVin()); // Update based on car VIN
                driverStmt.executeUpdate();
    
                System.out.println("Data successfully updated!");
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (conn != null) {
                        conn.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating profile data", e);
        }
    }
}





