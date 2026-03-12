package com.smarttutor.service;

import com.smarttutor.dto.LoginRequestDTO;
import com.smarttutor.entity.Hod;
import com.smarttutor.repository.HodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private HodRepository hodRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    // Simple validation method - no security context
    public boolean validateCredentials(String email, String password) {
        try {
            Hod hod = hodRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            return passwordEncoder.matches(password, hod.getPassword());
        } catch (Exception e) {
            return false;
        }
    }
}
