package com.Illia.controller;

import com.Illia.dao.DriverCarDAO;
import com.Illia.dto.DriverCarDTO;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/driver")
@RequiredArgsConstructor
public class DriverCarController {

    private final DriverCarDAO profileService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping
    public ResponseEntity<?> getDriverProfile(@RequestParam(name = "email") String email){
        System.out.println("DriverCar GET request received");
        System.out.println("sent email: " + email);

        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body("Email is required");
        }

        DriverCarDTO profile = profileService.getProfileByEmail(email);
        if (profile == null) {
            System.out.println("Profile does not exist");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Profile not found");
        }

        System.out.println("received profile: " + profile);
        return ResponseEntity.ok(profile);
    }

    @PostMapping
    public ResponseEntity<?> saveDriverProfile(@RequestBody Map<String, String> profileData) {
        System.out.println("DriverProfile POST request received");
        System.out.println(profileData);

        String email = profileData.get("email");
        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body("Email is required");
        }

        DriverCarDTO existingProfile = profileService.getProfileByEmail(email);
        try {
            DriverCarDTO profile = objectMapper.convertValue(profileData, DriverCarDTO.class);
            if (existingProfile == null) {
                System.out.println("Creating profile");
                profileService.createProfile(profile);
                return ResponseEntity.status(HttpStatus.CREATED).body("Profile created successfully");
            } else {
                System.out.println("Updating profile");
                profileService.updateProfile(profile);
                return ResponseEntity.ok("Profile updated successfully");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process profile data");
        }
    }
}
