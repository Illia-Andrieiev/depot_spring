package com.Illia.controller;

import com.Illia.dao.DriverCarDAO;
import com.Illia.dao.QueryDAO;
import com.Illia.dao.TripDAO;
import com.Illia.dto.DriverCarDTO;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/query/driver")
@RequiredArgsConstructor
public class DriverAssignController {

    private final DriverCarDAO profileService;

    private boolean hasDispatcherRole(OidcUser oidcUser) {
        List<String> roles = oidcUser.getClaim("https://example.com/roles/roles");
        return roles != null && roles.contains("dispatcher");
    }

    @GetMapping
    public ResponseEntity<?> getAvailableDrivers(@AuthenticationPrincipal OidcUser user,
                                                 @RequestParam(name = "id") int id) {
        System.out.println("DriverAssign GET request received");

        if (!hasDispatcherRole(user)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied: dispatcher role required.");
        }

        ArrayList<DriverCarDTO> profiles = profileService.getAvailableByQuery(id);
        if (profiles == null || profiles.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No available drivers found.");
        }

        return ResponseEntity.ok(profiles);
    }

    @PostMapping
    public ResponseEntity<?> assignDriverToTrip(@AuthenticationPrincipal OidcUser user,
                                                @RequestBody Map<String, Object> profileData) {
        System.out.println("DriverProfile POST request received");

        if (!hasDispatcherRole(user)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied: dispatcher role required.");
        }

        try {
            int driverId = Integer.parseInt(String.valueOf(profileData.get("driverId")));
            int queryId = Integer.parseInt(String.valueOf(profileData.get("queryId")));
            String email = String.valueOf(profileData.get("email"));

            QueryDAO queryDAO = new QueryDAO();
            TripDAO tripDAO = new TripDAO();

            queryDAO.updateQueryStatus(queryId, "assigned");
            tripDAO.createTrip(queryId, email, driverId);

            return ResponseEntity.status(HttpStatus.CREATED).body("Trip assigned successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to assign trip");
        }
    }
}
