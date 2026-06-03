package com.shoes.identity.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Health-check endpoint. Test qua Gateway: GET /api/identity/ping
 * Test truc tiep: GET http://localhost:8081/api/identity/ping
 */
@RestController
@RequestMapping("/api/identity")
public class PingController {

    @GetMapping("/ping")
    public Map<String, Object> ping() {
        return Map.of(
                "service", "identity-service",
                "status", "UP",
                "timestamp", System.currentTimeMillis()
        );
    }
}
