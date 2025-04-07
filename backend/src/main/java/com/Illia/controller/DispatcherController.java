package com.Illia.controller;

import com.Illia.dto.ProfileDTO;
import com.Illia.dao.ProfileDAO;
import com.Illia.dto.DispatcherDTO;
import com.Illia.config.DatabaseConfig;
import com.Illia.dao.DispatcherDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

public class DispatcherController extends HttpServlet {
    private final ProfileDAO profileService = new ProfileDAO();
    private final DispatcherDAO dispatcherService = new DispatcherDAO();

    @Override
    public void init() throws ServletException {
        super.init();
        System.out.println("dispatcher controller loaded!");
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.setHeader("Access-Control-Allow-Credentials", "true"); 
        response.setStatus(HttpServletResponse.SC_OK);
    }

private void sendJsonResponse(HttpServletResponse response, String status, String message) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    Map<String, String> responseMap = new HashMap<>();
    responseMap.put("status", status);
    responseMap.put("message", message);
    String jsonResponse = mapper.writeValueAsString(responseMap);
    response.getWriter().write(jsonResponse);
}

    @Override
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    System.out.println("Dispatcher POST request received");
    request.setCharacterEncoding("UTF-8");
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");

    BufferedReader reader = request.getReader();
    ObjectMapper objectMapper = new ObjectMapper();
    Map<String, String> uuidRequestMap = objectMapper.readValue(reader, Map.class);
    System.out.println(uuidRequestMap.toString());

    String email = uuidRequestMap.get("email");

    if (email == null || email.isEmpty()) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        sendJsonResponse(response, "error", "Email is required");
        return;
    }

    try (Connection conn = DatabaseConfig.getConnection()) {
        System.out.println("Connection opened");

        String uuid = uuidRequestMap.get("uuid");
        System.out.println("Parsed email: " + email);
        System.out.println("Parsed uuid: " + uuid);

        ProfileDTO existingProfile = profileService.getProfileByEmail(conn, email);
        System.out.println("Profile received: " + existingProfile);

        DispatcherDTO dispatcherProfile = dispatcherService.getDispatcherByEmail(conn, email);
        System.out.println("Dispatcher received: " + dispatcherProfile);

        if (dispatcherProfile == null) {
            System.out.println("Dispatcher not registered in DB");
            sendJsonResponse(response, "error", "You do not have dispatcher permissions. Ask admin if you should.");
        } else if (!dispatcherProfile.getUUID().equals(uuid)) {
            System.out.println("Wrong UUID");
            sendJsonResponse(response, "error", "Wrong UUID. Try again or contact admin.");
        } else if (existingProfile == null || existingProfile.isAnyFieldEmpty()) {
            System.out.println("Profile missing or has empty fields");
            sendJsonResponse(response, "error", "Profile is not complete. Please finish your registration.");
        } else {
            sendJsonResponse(response, "success", "Dispatcher access granted.");
        }

    } catch (SQLException e) {
        e.printStackTrace();
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        sendJsonResponse(response, "error", "SQL Error: " + e.getMessage());
    }
}

}
