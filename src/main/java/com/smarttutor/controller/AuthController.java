package com.smarttutor.controller;

import com.smarttutor.dto.LoginRequestDTO;
import com.smarttutor.entity.Hod;
import com.smarttutor.repository.HodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private HodRepository hodRepository;

    // ✅ TEST API
    @GetMapping("/test")
    public String test() {
        return "API is working - No Security!";
    }

    // ✅ REGISTER HOD
    @PostMapping("/register-hod")
    public ResponseEntity<?> registerHod(@RequestBody Hod hod) {
        if (hodRepository.existsByEmail(hod.getEmail())) {
            return ResponseEntity.badRequest().body("HOD already exists");
        }

        hod.setPassword(hashPassword(hod.getPassword()));
        hod.setActive(true);
        Hod saved = hodRepository.save(hod);
        return ResponseEntity.ok(saved);
    }

    // ✅ SIMPLE LOGIN - NO SESSION, NO SECURITY
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            String password = request.get("password");
            String role = request.get("role");
            
            // Validate input
            if (email == null || password == null || role == null) {
                return ResponseEntity.badRequest().body("Missing required fields");
            }
            
            // Find HOD by email
            Hod hod = hodRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Invalid credentials"));

            // Verify password
            if (!verifyPassword(password, hod.getPassword())) {
                throw new RuntimeException("Invalid credentials");
            }

            // ✅ RETURN USER DATA ONLY - NO SESSION, NO TOKEN
            return ResponseEntity.ok(Map.of(
                    "id", hod.getId(),
                    "name", hod.getName(),
                    "email", hod.getEmail(),
                    "role", "HOD"
            ));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid credentials");
        }
    }

    // ✅ SIMPLE LOGOUT - JUST FOR FRONTEND COMPATIBILITY
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok(Map.of("message", "Logout successful"));
    }

    // Simple password hashing (SHA-256)
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    // Simple password verification
    private boolean verifyPassword(String rawPassword, String hashedPassword) {
        return hashPassword(rawPassword).equals(hashedPassword);
    }
}
