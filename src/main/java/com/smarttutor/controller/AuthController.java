package com.smarttutor.controller;

import com.smarttutor.dto.LoginRequestDTO;
import com.smarttutor.entity.Hod;
import com.smarttutor.entity.Teacher;
import com.smarttutor.entity.Student;
import com.smarttutor.repository.HodRepository;
import com.smarttutor.repository.TeacherRepository;
import com.smarttutor.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private HodRepository hodRepository;
    
    @Autowired
    private TeacherRepository teacherRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

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

        hod.setPassword(passwordEncoder.encode(hod.getPassword()));
        hod.setActive(true);
        Hod saved = hodRepository.save(hod);
        return ResponseEntity.ok(saved);
    }

    // ✅ AUTO ROLE DETECTION LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");
        
        if (email == null || password == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email and password are required"));
        }
        
        try {
            // Check HOD
            Hod hod = hodRepository.findByEmail(email).orElse(null);
            if (hod != null) {
                try {
                    if (hod.getPassword() != null && passwordEncoder.matches(password, hod.getPassword())) {
                        return ResponseEntity.ok(Map.of(
                            "id", hod.getId(),
                            "name", hod.getName(),
                            "email", hod.getEmail(),
                            "role", "HOD"
                        ));
                    }
                } catch (Exception e) {
                    System.err.println("HOD password matching error: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            
            // Check Teacher
            Teacher teacher = teacherRepository.findByEmail(email).orElse(null);
            if (teacher != null) {
                try {
                    if (teacher.getPassword() != null && passwordEncoder.matches(password, teacher.getPassword())) {
                        return ResponseEntity.ok(Map.of(
                            "id", teacher.getId(),
                            "name", teacher.getName(),
                            "email", teacher.getEmail(),
                            "role", "TEACHER"
                        ));
                    }
                } catch (Exception e) {
                    System.err.println("Teacher password matching error: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            
            // Check Student
            Student student = studentRepository.findByEmail(email).orElse(null);
            if (student != null) {
                try {
                    if (student.getPassword() != null && passwordEncoder.matches(password, student.getPassword())) {
                        return ResponseEntity.ok(Map.of(
                            "id", student.getId(),
                            "name", student.getName(),
                            "email", student.getEmail(),
                            "role", "STUDENT"
                        ));
                    }
                } catch (Exception e) {
                    System.err.println("Student password matching error: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid email or password"));
        } catch (Exception e) {
            System.err.println("Login exception: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "Internal server error: " + e.getMessage()));
        }
    }

    // ✅ SIMPLE LOGOUT - JUST FOR FRONTEND COMPATIBILITY
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok(Map.of("message", "Logout successful"));
    }

    // ✅ RESET TEACHER PASSWORD
    @PostMapping("/reset-teacher-password")
    public ResponseEntity<?> resetTeacherPassword(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            String newPassword = request.get("password");
            
            Teacher teacher = teacherRepository.findByEmail(email).orElse(null);
            if (teacher == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Teacher not found"));
            }
            
            teacher.setPassword(passwordEncoder.encode(newPassword));
            teacherRepository.save(teacher);
            
            return ResponseEntity.ok(Map.of(
                "message", "Password reset successfully",
                "email", email,
                "newPassword", newPassword
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ CREATE TEST TEACHER WITH KNOWN PASSWORD
    @PostMapping("/create-test-teacher")
    public ResponseEntity<?> createTestTeacher() {
        try {
            Teacher teacher = new Teacher();
            teacher.setName("Test Teacher");
            teacher.setEmail("teacher@smarttutor.com");
            teacher.setPassword("teacher123"); // This will be encoded
            teacher.setPhone("1234567890");
            teacher.setActive(true);
            teacher.setCreatedAt(java.time.LocalDateTime.now());
            
            teacher.setPassword(passwordEncoder.encode(teacher.getPassword()));
            Teacher saved = teacherRepository.save(teacher);
            
            return ResponseEntity.ok(Map.of(
                "message", "Test teacher created successfully",
                "email", "teacher@smarttutor.com",
                "password", "teacher123"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }


}
