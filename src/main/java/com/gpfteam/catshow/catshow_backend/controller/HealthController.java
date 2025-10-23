package com.gpfteam.catshow.catshow_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.sql.DataSource;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // Pro development, v produkci specifikovat domény
public class HealthController {

    @Autowired
    private DataSource dataSource;

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "Cat Show Backend");
        response.put("timestamp", System.currentTimeMillis());
        
        // Test DB connection
        try (Connection connection = dataSource.getConnection()) {
            response.put("database", "Connected");
            response.put("databaseProductName", connection.getMetaData().getDatabaseProductName());
        } catch (Exception e) {
            response.put("database", "Disconnected");
            response.put("error", e.getMessage());
            return ResponseEntity.status(503).body(response);
        }
        
        return ResponseEntity.ok(response);
    }
}