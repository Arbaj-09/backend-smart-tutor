package com.smarttutor.controller;

import com.smarttutor.dto.FCMTokenRequest;
import com.smarttutor.entity.Teacher;
import com.smarttutor.entity.Student;
import com.smarttutor.entity.Hod;
import com.smarttutor.entity.FcmToken;
import com.smarttutor.service.TeacherService;
import com.smarttutor.service.StudentService;
import com.smarttutor.service.HodService;
import com.smarttutor.service.NotificationService;
import com.smarttutor.repository.StudentRepository;
import com.smarttutor.exception.ResourceNotFoundException;
import com.smarttutor.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fcm")
@CrossOrigin(origins = "*", maxAge = 3600)
public class FCMTokenController {
    
    @Autowired
    private TeacherService teacherService;
    
    @Autowired
    private StudentService studentService;
    
    @Autowired
    private HodService hodService;
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private NotificationService notificationService;
    
    @PostMapping("/token")
    public ResponseEntity<?> saveFCMToken(@RequestBody FCMTokenRequest request) {
        try {
            System.out.println("📱 FCM: Received token save request");
            System.out.println("📱 FCM: Request data - UserId: " + request.getUserId() + 
                             ", Role: " + request.getRole() + 
                             ", Token: " + (request.getToken() != null ? request.getToken().substring(0, 20) + "..." : "null"));
            
            // Validate request data
            if (request.getUserId() == null) {
                System.out.println("❌ FCM: User ID is null");
                return ResponseEntity.badRequest().body("User ID cannot be null");
            }
            
            if (request.getRole() == null || request.getRole().trim().isEmpty()) {
                System.out.println("❌ FCM: Role is null or empty");
                return ResponseEntity.badRequest().body("Role cannot be null or empty");
            }
            
            if (request.getToken() == null || request.getToken().trim().isEmpty()) {
                System.out.println("❌ FCM: Token is null or empty");
                return ResponseEntity.badRequest().body("Token cannot be null or empty");
            }
            
            FcmToken token = new FcmToken();
            token.setUserId(request.getUserId());
            token.setToken(request.getToken());
            token.setUserRole(Role.valueOf(request.getRole().toUpperCase()));
            token.setPlatform(FcmToken.Platform.WEB);

            notificationService.saveFCMToken(token);

            System.out.println("✅ FCM: Token saved successfully for user " + request.getUserId() + " role " + request.getRole());

            return ResponseEntity.ok("FCM token saved successfully");
        } catch (IllegalArgumentException e) {
            System.out.println("❌ FCM: Invalid role - " + e.getMessage());
            return ResponseEntity.badRequest().body("Invalid role: " + request.getRole());
        } catch (Exception e) {
            System.out.println("❌ FCM: Error saving token - " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body("Error saving FCM token: " + e.getMessage());
        }
    }
    
    @DeleteMapping("/token")
    public ResponseEntity<?> removeFCMToken(@RequestParam String role, @RequestParam Long userId) {
        try {
            switch (role.toUpperCase()) {
                case "TEACHER":
                    Teacher teacher = teacherService.getTeacherById(userId);
                    teacher.setFcmToken(null);
                    teacherService.updateTeacher(userId, teacher);
                    break;
                    
                case "STUDENT":
                    Student student = studentService.getStudentById(userId);
                    student.setFcmToken(null);
                    studentRepository.save(student);
                    break;
                    
                case "HOD":
                    Hod hod = hodService.getHodById(userId);
                    hod.setFcmToken(null);
                    hodService.updateHod(userId, hod);
                    break;
                    
                default:
                    return ResponseEntity.badRequest().body("Invalid role: " + role);
            }
            
            return ResponseEntity.ok().body("FCM token removed successfully");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error removing FCM token: " + e.getMessage());
        }
    }
}
