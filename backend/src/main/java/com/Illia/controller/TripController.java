package com.Illia.controller;

import com.Illia.dto.TripDTO;
import com.Illia.dao.TripDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Map;

public class TripController extends HttpServlet {
    private final TripDAO tripService = new TripDAO();

    @Override
    public void init() throws ServletException {
        super.init();
        System.out.println("DriverCar controller loaded!");
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.setHeader("Access-Control-Allow-Credentials", "true"); 
        response.setStatus(HttpServletResponse.SC_OK);
    }

   
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Trip POST request received");
        request.setCharacterEncoding("UTF-8");
    
        // Чтение JSON тела
        BufferedReader reader = request.getReader();
        StringBuilder jsonBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonBuilder.append(line);
        }
        String json = jsonBuilder.toString();
    
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    
        // Парсим JSON в Map
        Map<String, Object> profileData = objectMapper.readValue(json, Map.class);
        System.out.println("Parsed map: " + profileData);
    
        String type = (String) profileData.get("type");
        String email = (String) profileData.get("email");
    
        if (email == null || email.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Email is required\"}");
            return;
        }
    
        if ("get_assigned".equals(type)) {
            System.out.println("Getting assigned trips for: " + email);
            ArrayList<TripDTO> trips = tripService.getAssignedTrips(email);
    
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(
                    trips != null ? trips : new ArrayList<>()
            ));
    
        } else if ("finish".equals(type)) {
            try {
                System.out.println("Finishing trip...");
    
                int id = Integer.parseInt(profileData.get("tripId").toString());
                int mileage = Integer.parseInt(profileData.get("mileage").toString());
                boolean serviceable = Boolean.parseBoolean(profileData.get("serviceable").toString());
    
                String endDatetimeStr = (String) profileData.get("endDatetime");
                LocalDateTime endDatetime = LocalDateTime.parse(endDatetimeStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    
                // Обновление поездки
                tripService.updateTrip(id, email, mileage, endDatetime, serviceable);
                System.out.println("Trip updated successfully");
    
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/json");
                response.getWriter().write("{\"message\": \"Trip updated successfully\"}");
            } catch (Exception e) {
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Failed to update trip\"}");
            }
    
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Invalid request type\"}");
        }
    }
}
