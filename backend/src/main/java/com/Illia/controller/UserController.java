package com.Illia.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RestController
@RequestMapping("/api")  // Add this base path to match your context path
public class UserController {
    private static final Logger logger = LogManager.getLogger(UserController.class);

    @GetMapping("/userinfo")
    public Map<String, Object> user(Principal principal) {
        logger.info("Mapping user info");
        return Map.of("name", principal.getName());
    }

    @GetMapping("/public")
    public String publicEndpoint() {
        logger.info("Mapping public endpoint");
        return "This is a public endpoint";
    }
}
