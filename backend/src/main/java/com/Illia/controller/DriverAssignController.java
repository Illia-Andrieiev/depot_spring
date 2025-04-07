package com.Illia.controller;

import com.Illia.dto.DriverCarDTO;
import com.Illia.dto.QueryDTO;
import com.Illia.dao.DriverCarDAO;
import com.Illia.dao.QueryDAO;
import com.Illia.dao.TripDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Map;

public class DriverAssignController extends HttpServlet {
    private final DriverCarDAO profileService = new DriverCarDAO();

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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("DriverAssign GET request received");

        System.out.println(request);
        int id = Integer.parseInt(request.getParameter("id"));
        
        System.out.println("sent query iq: " + id);

        ArrayList<DriverCarDTO> profile = profileService.getAvailableByQuery(id);
  
        if (profile == null) {
            System.out.println("Profile do not exist already");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("Profile not found");
            return;
        }
        System.out.println("recieved profile: " + profile.toString());

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(profile));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("DriverProfile POST request received");
        request.setCharacterEncoding("UTF-8");
        BufferedReader reader = request.getReader();
        StringBuilder jsonBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonBuilder.append(line);
        }
        String json = jsonBuilder.toString();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> profileData = objectMapper.readValue(json, Map.class);
        System.out.println(profileData.toString());
        String driverIdS = String.valueOf(profileData.get("driverId"));
        String queryIdS = String.valueOf(profileData.get("queryId"));
        String email = String.valueOf(profileData.get("email"));
        System.out.println("Parsed values:");
        System.out.println("driverIdS = " + driverIdS);
        System.out.println("queryIdS = " + queryIdS);
        System.out.println("email = " + email);
        int driverId = Integer.parseInt(driverIdS);
        int queryId = Integer.parseInt(queryIdS);
        QueryDAO queryDAO = new QueryDAO();
        System.out.println("Update status");
        queryDAO.updateQueryStatus(queryId,"assigned");
        TripDAO tripDAO = new TripDAO();
        System.out.println("Assigning trip");
        tripDAO.createTrip(queryId, email , driverId);
        response.setStatus(HttpServletResponse.SC_CREATED);
        response.getWriter().write("Trip assigned successfully");
    }
}
