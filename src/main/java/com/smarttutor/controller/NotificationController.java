package com.smarttutor.controller;

import com.smarttutor.entity.Notification;
import com.smarttutor.entity.FcmToken;
import com.smarttutor.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "*", maxAge = 3600)
public class NotificationController {
    
    @Autowired
    private NotificationService notificationService;
    
    @PostMapping
    public ResponseEntity<Notification> createNotification(@RequestBody Notification notification) {
        try {
            Notification createdNotification = notificationService.createNotification(notification);
            return ResponseEntity.ok(createdNotification);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping
    public ResponseEntity<List<Notification>> getAllNotifications() {
        List<Notification> notifications = notificationService.getAllNotifications();
        return ResponseEntity.ok(notifications);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Notification> getNotificationById(@PathVariable Long id) {
        try {
            Notification notification = notificationService.getNotificationById(id);
            return ResponseEntity.ok(notification);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/role/{role}/user/{userId}")
    public ResponseEntity<List<Notification>> getNotificationsByUser(
            @PathVariable String role, @PathVariable Long userId) {
        List<Notification> notifications = notificationService.getNotificationsByUser(role, userId);
        return ResponseEntity.ok(notifications);
    }
    
    @GetMapping("/unread/role/{role}/user/{userId}")
    public ResponseEntity<List<Notification>> getUnreadNotificationsByUser(
            @PathVariable String role, @PathVariable Long userId) {
        List<Notification> notifications = notificationService.getUnreadNotificationsByUser(role, userId);
        return ResponseEntity.ok(notifications);
    }
    
    @GetMapping("/unread/count/role/{role}/user/{userId}")
    public ResponseEntity<Long> getUnreadNotificationCount(
            @PathVariable String role, @PathVariable Long userId) {
        Long count = notificationService.getUnreadNotificationCount(role, userId);
        return ResponseEntity.ok(count);
    }
    
    @PutMapping("/{id}/read")
    public ResponseEntity<?> markNotificationAsRead(@PathVariable Long id) {
        try {
            notificationService.markNotificationAsRead(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/read-all/role/{role}/user/{userId}")
    public ResponseEntity<?> markAllNotificationsAsRead(
            @PathVariable String role, @PathVariable Long userId) {
        notificationService.markAllNotificationsAsRead(role, userId);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/fcm/token")
    public ResponseEntity<?> saveFCMToken(@RequestBody FcmToken fcmToken) {
        try {
            FcmToken savedToken = notificationService.saveFCMToken(fcmToken);
            return ResponseEntity.ok(savedToken);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
