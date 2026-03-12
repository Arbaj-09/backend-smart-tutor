package com.smarttutor.service;

import com.smarttutor.entity.ActivityLog;
import com.smarttutor.enums.Role;
import com.smarttutor.repository.ActivityLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ActivityLogService {
    
    @Autowired
    private ActivityLogRepository activityLogRepository;
    
    public ActivityLog logActivity(Role userRole, Long userId, String action, String description) {
        ActivityLog activityLog = new ActivityLog(userRole, userId, action, description);
        return activityLogRepository.save(activityLog);
    }
    
    public List<ActivityLog> getAllActivityLogs() {
        return activityLogRepository.findAll();
    }
    
    public List<ActivityLog> getActivityLogsByUser(Role userRole, Long userId) {
        return activityLogRepository.findByUserRoleAndUserId(userRole, userId);
    }
    
    public List<ActivityLog> getActivityLogsByRole(Role role) {
        return activityLogRepository.findByUserRole(role);
    }
    
    public List<ActivityLog> getActivityLogsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return activityLogRepository.findByDateRange(startDate, endDate);
    }
    
    public ActivityLog getActivityLogById(Long id) {
        return activityLogRepository.findById(id).orElse(null);
    }
    
    public List<ActivityLog> getActivityLogsByAction(String action) {
        return activityLogRepository.findByAction(action);
    }
    
    public List<ActivityLog> getActivityLogsByRoleAndDateRange(Role role, LocalDateTime startDate, LocalDateTime endDate) {
        return activityLogRepository.findByUserRoleAndDateRange(role, startDate, endDate);
    }
    
    public List<ActivityLog> getRecentActivitiesByUser(Role role, Long userId, int limit) {
        return activityLogRepository.findByUserRoleAndUserId(role, userId)
                .stream()
                .limit(limit)
                .toList();
    }
}
