package com.Illia.dao;


import com.Illia.config.DatabaseConfig;
import com.Illia.dto.QueryDTO;
import com.Illia.dto.TripDTO;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class TripDAO {

    private static final String GET_QUERY_BY_ID = "SELECT * FROM driver_query WHERE id = ? AND (status ='new' or status ='expired')";



    public void createTrip(int queryId, String dispatcherEmail, int driverId) {
        System.out.println("Creating query in database");
       
        try (Connection conn = DatabaseConfig.getConnection();) {
            try {
                // Select statement for the 'dispatcher' table
                String getDispatcherId = "SELECT id FROM dispatcher WHERE email = ?";
                PreparedStatement dispatcherStmt = conn.prepareStatement(getDispatcherId);
                dispatcherStmt.setString(1, dispatcherEmail); // Example email
                ResultSet rs = dispatcherStmt.executeQuery();
                UUID dispatcherId = null;
                if (rs.next()) {
                    dispatcherId = (UUID) rs.getObject("id"); // 👈 корректно получаем UUID
                }

                PreparedStatement stmt = conn.prepareStatement(GET_QUERY_BY_ID);
                stmt.setInt(1, queryId);
                rs = stmt.executeQuery();
                System.out.println("Trying to receive query [" + queryId + "]");
                QueryDTO queryTrip = null;
                 if (rs.next()) {
                        queryTrip = new QueryDTO(
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
                }
   
                // Insert statement for the 'trip' table
                String insertTrip = "INSERT INTO trip (query_id, dispatcher_id, driver_id) VALUES (?, ?, ?)";
                PreparedStatement carStmt = conn.prepareStatement(insertTrip);
                carStmt.setInt(1, queryId); // query id
                carStmt.setObject(2, dispatcherId); // UUID 
                carStmt.setInt(3, driverId); // Driver id
        
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
    public ArrayList<TripDTO> getAssignedTrips(String email){
        System.out.println("DB get trip");
        try (Connection conn = DatabaseConfig.getConnection();) {
            String driverQuery = "SELECT id FROM driver WHERE email = ?"; 
            PreparedStatement dstmt = conn.prepareStatement(driverQuery);
            dstmt.setString(1, email);
            ResultSet rs = dstmt.executeQuery();
            int driverId = -1;
            if(rs.next()){
                driverId = rs.getInt("id");
            }
            System.out.println("Driver id: " + driverId);
            String tripQuery = "SELECT * FROM trip WHERE driver_id = ?"; 
            PreparedStatement tstmt = conn.prepareStatement(tripQuery);
            tstmt.setInt(1, driverId);
            rs = tstmt.executeQuery();
   
            ArrayList<TripDTO> trips = new ArrayList<>(); 
            while (rs.next()) {
                TripDTO newDto = new TripDTO();
                newDto.setQueryId(rs.getInt("query_id") );
                newDto.setId(rs.getInt("id"));
                newDto.setQueryId(rs.getInt("query_id"));
                System.out.println("recieved trip: " + newDto.toString());
                trips.add(newDto);
                }
         // Получаем дополнительные данные по каждому query, и удаляем ненужные
        Iterator<TripDTO> iterator = trips.iterator();
        while (iterator.hasNext()) {
            TripDTO trip = iterator.next();
            String qQuery = "SELECT * FROM driver_query WHERE id = ?";
            PreparedStatement qstmt = conn.prepareStatement(qQuery);
            qstmt.setInt(1, trip.getQueryId());
            rs = qstmt.executeQuery();

            if (rs.next()) {
                QueryDTO queryTrip = new QueryDTO(
                    rs.getInt("id"),
                    rs.getTimestamp("add_datetime").toLocalDateTime(),
                    rs.getTimestamp("start_datetime").toLocalDateTime(),
                    rs.getString("email"),
                    rs.getString("start_location"),
                    rs.getString("destination"),
                    rs.getInt("driver_experience"),
                    rs.getString("car_type"),
                    rs.getInt("max_car_mileage"),
                    rs.getString("status")
                );

             
                trip.setDestination(queryTrip.getDestination());
                trip.setEmail(queryTrip.getEmail());
                trip.setStartDatetime(queryTrip.getStartDatetime());
                trip.setStartLocation(queryTrip.getStartLocation());

            
                if (!"assigned".equalsIgnoreCase(queryTrip.getStatus())) {
                    iterator.remove();
                }

            } else {
    
                iterator.remove();
            }
        }
        return trips;
     } catch (SQLException e) {
            System.out.println("Error retrieving query data");
            e.printStackTrace();
            throw new RuntimeException("Error retrieving query data", e);
        }
        
    }
    public void updateTrip(int id, String email, int mileage, LocalDateTime endDatetime, boolean serviceable) {
        System.out.println("Creating query in database");
       
        try (Connection conn = DatabaseConfig.getConnection();) {
            try {
                String getVin = "SELECT car_vin FROM driver WHERE email = ?";
                PreparedStatement vinStmt = conn.prepareStatement(getVin);
                vinStmt.setString(1, email);
                ResultSet rs = vinStmt.executeQuery();
                String vin = "";
                if(rs.next()){
                    vin = rs.getString("car_vin");
                }
                 // Update statement for the 'car' table
                 String updateCar = "UPDATE car SET  mileage = mileage + ?, is_serviceable = ? WHERE vin = ?";
                 PreparedStatement carStmt = conn.prepareStatement(updateCar);
                 carStmt.setInt(1, mileage);
                 carStmt.setBoolean(2, serviceable);
                 carStmt.setString(3, vin);
                 carStmt.executeUpdate();
     
                 // Update statement for the 'driver' table
                 String updateTrip = "UPDATE trip SET end_datetime = ?, mileage = ?, end_car_serviceability = ? WHERE id = ?";
                 PreparedStatement tripStmt = conn.prepareStatement(updateTrip);
                 tripStmt.setTimestamp(1, java.sql.Timestamp.valueOf(endDatetime));
                 tripStmt.setInt(2, mileage);
                 tripStmt.setBoolean(3, serviceable);
                 tripStmt.setInt(4, id);
                 tripStmt.executeUpdate(); 

                String getid = "SELECT query_id FROM trip WHERE id = ?";
                PreparedStatement idStmt = conn.prepareStatement(getid);
                idStmt.setInt(1, id);
                rs = idStmt.executeQuery();
                int q_id = 0;
                if(rs.next()){
                    q_id = rs.getInt("query_id");
                }
                 String UPDATE_QUERY = "UPDATE driver_query SET status = ? WHERE id = ?";
                PreparedStatement stmt = conn.prepareStatement(UPDATE_QUERY);
                    stmt.setString(1, "finished");
                    stmt.setInt(2, q_id);
                    stmt.executeUpdate(); // Execute the query
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
}

