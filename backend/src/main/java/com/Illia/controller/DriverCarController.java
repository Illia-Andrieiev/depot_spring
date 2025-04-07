package com.Illia.controller;

import com.Illia.dto.DriverCarDTO;
import com.Illia.dao.DriverCarDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.util.Map;

public class DriverCarController extends HttpServlet {
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
        System.out.println("DriverCar GET request received");

        String email = request.getParameter("email");
        System.out.println("sent email: " + email);
        if (email == null || email.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Email is required");
            return;
        }

        DriverCarDTO profile = profileService.getProfileByEmail(email);
  
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
        Map<String, String> profileData = objectMapper.readValue(json, Map.class);
        System.out.println(profileData.toString());
        String email = profileData.get("email");
        if (email == null || email.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Email is required");
            return;
        }
        DriverCarDTO existingProfile = profileService.getProfileByEmail(email);
        System.out.println("existing profile: ");
        System.out.println(existingProfile==null?existingProfile:existingProfile.toString());
        if (existingProfile == null) {
            // Создаём новый профиль
            System.out.println("creating profile");
            DriverCarDTO newProfile = objectMapper.readValue(json, DriverCarDTO.class);
            profileService.createProfile(newProfile);
            System.out.println("creating profile created");
            response.setStatus(HttpServletResponse.SC_CREATED);
            response.getWriter().write("Profile created successfully");
        } else {
            existingProfile = objectMapper.readValue(json, DriverCarDTO.class);
            
            System.out.println("changing profile");
            System.out.println(profileData.toString());
            System.out.println(existingProfile.toString());
            profileService.updateProfile(existingProfile);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("Profile updated successfully");
        }
    }
}
