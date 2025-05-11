package com.Illia.controller;

import com.Illia.dto.TripDTO;
import com.Illia.dao.TripDAO;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/api/trip")
public class TripController {

    private final TripDAO tripService = new TripDAO();

    @PostMapping
    public ResponseEntity<?> handleTripRequest(@RequestBody Map<String, Object> profileData) {
        System.out.println("Trip POST request received");
        System.out.println("Parsed map: " + profileData);

        String type = (String) profileData.get("type");
        String email = (String) profileData.get("email");

        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email is required"));
        }

        switch (type) {
            case "get_assigned":
                System.out.println("Getting assigned trips for: " + email);
                List<TripDTO> trips = tripService.getAssignedTrips(email);
                return ResponseEntity.ok(trips != null ? trips : new ArrayList<>());

            case "finish":
                try {
                    System.out.println("Finishing trip...");

                    int id = Integer.parseInt(profileData.get("tripId").toString());
                    int mileage = Integer.parseInt(profileData.get("mileage").toString());
                    boolean serviceable = Boolean.parseBoolean(profileData.get("serviceable").toString());

                    String endDatetimeStr = (String) profileData.get("endDatetime");
                    LocalDateTime endDatetime = LocalDateTime.parse(endDatetimeStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);

                    tripService.updateTrip(id, email, mileage, endDatetime, serviceable);
                    System.out.println("Trip updated successfully");

                    return ResponseEntity.ok(Map.of("message", "Trip updated successfully"));
                } catch (Exception e) {
                    e.printStackTrace();
                    return ResponseEntity.internalServerError().body(Map.of("error", "Failed to update trip"));
                }

            default:
                return ResponseEntity.badRequest().body(Map.of("error", "Invalid request type"));
        }
    }
}
