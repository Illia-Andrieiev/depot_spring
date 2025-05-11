package com.Illia.controller;

import com.Illia.dto.ProfileDTO;
import com.Illia.dao.ProfileDAO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

@RestController
@RequestMapping("/api/profile")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class ProfileController {

    private final ProfileDAO profileService;

    public ProfileController(ProfileDAO profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    public ResponseEntity<?> getProfile(@RequestParam(name = "email") String email)
    {
        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body("Email is required");
        }

        ProfileDTO profile = profileService.getProfileByEmail(email);
        if (profile == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Profile not found");
        }

        return ResponseEntity.ok(profile);
    }

    @PostMapping
    public ResponseEntity<String> createOrUpdateProfile(@RequestBody Map<String, String> profileData) {
        String email = profileData.get("email");
        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body("Email is required");
        }

        ProfileDTO existingProfile = profileService.getProfileByEmail(email);
        if (existingProfile == null) {
            ProfileDTO newProfile = new ProfileDTO();
            newProfile.setEmail(email);
            profileData.forEach((key, value) -> {
                if (!"email".equals(key) && value != null && !value.isEmpty()) {
                    newProfile.setField(key, value);
                }
            });
            profileService.createProfile(newProfile);
            return ResponseEntity.status(HttpStatus.CREATED).body("Profile created successfully");
        } else {
            profileData.forEach((key, value) -> {
                if (!"email".equals(key)) {
                    existingProfile.setField(key, value);
                }
            });
            profileService.updateProfile(existingProfile);
            return ResponseEntity.ok("Profile updated successfully");
        }
    }

    // Optional: You can define an endpoint to handle OPTIONS explicitly if needed
    @RequestMapping(method = RequestMethod.OPTIONS)
    public void handleOptions(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
