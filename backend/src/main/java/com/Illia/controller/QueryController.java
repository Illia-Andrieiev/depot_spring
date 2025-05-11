package com.Illia.controller;

import com.Illia.dto.QueryDTO;
import com.Illia.config.DatabaseConfig;
import com.Illia.dao.QueryDAO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/api/query")
public class QueryController {

    private final QueryDAO queryService = new QueryDAO();
    private static final Logger logger = LogManager.getLogger(QueryController.class);

    @PostMapping
    public ResponseEntity<?> handleQuery(@RequestBody Map<String, String> requestData) {
        logger.info("Query POST request received: {}", requestData);

        String email = requestData.get("email");
        String type = requestData.get("type");

        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("status", "error", "message", "Email is required"));
        }

         try (Connection conn = DatabaseConfig.getConnection()) { 
            
            switch (type) {
                case "get_id":
                    int editId = Integer.parseInt(requestData.get("editId"));
                    
                    QueryDTO queryById = queryService.getQueryByID(conn,editId);
                    if (queryById == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(Map.of("status", "error", "message", "Wrong id."));
                    }
                    return ResponseEntity.ok(queryById);

                case "get_email":
                    return ResponseEntity.ok(queryService.getQueryByEmail(conn,email));

                case "get_new":
                    return ResponseEntity.ok(queryService.getNew(conn));

                case "add":
                    QueryDTO newQuery = buildQueryDTO(requestData, email);
                    if (newQuery.getAddDatetime().isAfter(newQuery.getStartDatetime())) {
                        logger.warn("Query already expired");
                        return ResponseEntity.badRequest().body("Query already expired");
                    }
                    queryService.createQuery(newQuery);
                    return ResponseEntity.status(HttpStatus.CREATED).body("Query created successfully");

                case "delete":
                    int deleteId = Integer.parseInt(requestData.get("deleteId"));
                    queryService.deleteQuery(deleteId);
                    return ResponseEntity.ok("Query deleted successfully");

                case "update":
                    QueryDTO updatedQuery = buildQueryDTO(requestData, email);
                    updatedQuery.setId(Integer.parseInt(requestData.get("editId")));
                    if (updatedQuery.getAddDatetime().isAfter(updatedQuery.getStartDatetime())) {
                        logger.warn("Query already expired");
                        return ResponseEntity.badRequest().body("Query already expired");
                    }
                    queryService.updateQuery(updatedQuery);
                    return ResponseEntity.status(HttpStatus.CREATED).body("Query updated successfully");

                default:
                    return ResponseEntity.badRequest().body("Unknown request type: " + type);
            }
        } catch (Exception e) {
            logger.error("Error processing query", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", "error", "message", "Server error: " + e.getMessage()));
        }
    }

    private QueryDTO buildQueryDTO(Map<String, String> data, String email) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        QueryDTO dto = new QueryDTO();
        dto.setCarType(data.get("car_type"));
        dto.setDestination(data.get("destination"));
        dto.setEmail(email);
        dto.setDriverExperience(Integer.parseInt(data.get("driver_experience")));
        dto.setMaxCarMileage(Integer.parseInt(data.get("max_car_mileage")));
        dto.setStatus("new");
        dto.setStartDatetime(LocalDateTime.parse(data.get("start_datetime"), formatter));
        dto.setAddDatetime(LocalDateTime.now());
        dto.setStartLocation(data.get("start_location"));
        return dto;
    }
}
