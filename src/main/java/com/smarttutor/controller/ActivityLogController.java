package com.smarttutor.controller;

import com.smarttutor.entity.ActivityLog;
import com.smarttutor.enums.Role;
import com.smarttutor.service.ActivityLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/activity-logs")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ActivityLogController {
    
    @Autowired
    private ActivityLogService activityLogService;
    
    @PostMapping
    public ResponseEntity<ActivityLog> logActivity(@RequestBody ActivityLog activityLog) {
        try {
            ActivityLog loggedActivity = activityLogService.logActivity(
                activityLog.getUserRole(),
                activityLog.getUserId(),
                activityLog.getAction(),
                activityLog.getDescription()
            );
            return ResponseEntity.ok(loggedActivity);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping
    public ResponseEntity<List<ActivityLog>> getAllActivityLogs() {
        List<ActivityLog> logs = activityLogService.getAllActivityLogs();
        return ResponseEntity.ok(logs);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ActivityLog> getActivityLogById(@PathVariable Long id) {
        try {
            ActivityLog activityLog = activityLogService.getActivityLogById(id);
            return ResponseEntity.ok(activityLog);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/role/{role}/user/{userId}")
    public ResponseEntity<List<ActivityLog>> getActivityLogsByUser(
            @PathVariable String role, @PathVariable Long userId) {
        try {
            Role roleEnum = Role.valueOf(role.toUpperCase());
            List<ActivityLog> logs = activityLogService.getActivityLogsByUser(roleEnum, userId);
            return ResponseEntity.ok(logs);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/role/{role}")
    public ResponseEntity<List<ActivityLog>> getActivityLogsByRole(@PathVariable String role) {
        try {
            Role roleEnum = Role.valueOf(role.toUpperCase());
            List<ActivityLog> logs = activityLogService.getActivityLogsByRole(roleEnum);
            return ResponseEntity.ok(logs);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/action/{action}")
    public ResponseEntity<List<ActivityLog>> getActivityLogsByAction(@PathVariable String action) {
        List<ActivityLog> logs = activityLogService.getActivityLogsByAction(action);
        return ResponseEntity.ok(logs);
    }
    
    @GetMapping("/date-range")
    public ResponseEntity<List<ActivityLog>> getActivityLogsByDateRange(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime endDate) {
        List<ActivityLog> logs = activityLogService.getActivityLogsByDateRange(startDate, endDate);
        return ResponseEntity.ok(logs);
    }
    
    @GetMapping("/role/{role}/date-range")
    public ResponseEntity<List<ActivityLog>> getActivityLogsByRoleAndDateRange(
            @PathVariable String role,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime endDate) {
        try {
            Role roleEnum = Role.valueOf(role.toUpperCase());
            List<ActivityLog> logs = activityLogService.getActivityLogsByRoleAndDateRange(roleEnum, startDate, endDate);
            return ResponseEntity.ok(logs);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
