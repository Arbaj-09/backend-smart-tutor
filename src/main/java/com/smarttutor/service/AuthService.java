package com.smarttutor.service;

import com.smarttutor.dto.LoginRequestDTO;
import com.smarttutor.entity.Hod;
import com.smarttutor.repository.HodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

@Service
public class AuthService {

    @Autowired
    private HodRepository hodRepository;

    // Simple validation method - no security context
    public boolean validateCredentials(String email, String password) {
        try {
            Hod hod = hodRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            return verifyPassword(password, hod.getPassword());
        } catch (Exception e) {
            return false;
        }
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
