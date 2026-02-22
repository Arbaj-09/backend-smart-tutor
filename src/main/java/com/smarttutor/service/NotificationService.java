package com.smarttutor.service;

import com.smarttutor.entity.Notification;
import com.smarttutor.entity.FcmToken;
import com.smarttutor.repository.NotificationRepository;
import com.smarttutor.repository.FcmTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {
    
    @Autowired
    private NotificationRepository notificationRepository;
    
    @Autowired
    private FcmTokenRepository fcmTokenRepository;
    
    public Notification createNotification(Notification notification) {
        return notificationRepository.save(notification);
    }
    
    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }
    
    public Notification getNotificationById(Long id) {
        return notificationRepository.findById(id).orElse(null);
    }
    
    public List<Notification> getNotificationsByUser(String role, Long userId) {
        return notificationRepository.findByRecipientRoleAndUserId(
                com.smarttutor.enums.Role.valueOf(role), userId);
    }
    
    public List<Notification> getUnreadNotificationsByUser(String role, Long userId) {
        return notificationRepository.findUnreadByRecipientRoleAndUserId(
                com.smarttutor.enums.Role.valueOf(role), userId);
    }
    
    public Long getUnreadNotificationCount(String role, Long userId) {
        return notificationRepository.countUnreadByRecipientRoleAndUserId(
                com.smarttutor.enums.Role.valueOf(role), userId);
    }
    
    public void markNotificationAsRead(Long id) {
        Notification notification = getNotificationById(id);
        if (notification != null) {
            notification.setIsRead(true);
            notificationRepository.save(notification);
        }
    }
    
    public void markAllNotificationsAsRead(String role, Long userId) {
        List<Notification> notifications = getUnreadNotificationsByUser(role, userId);
        for (Notification notification : notifications) {
            notification.setIsRead(true);
        }
        notificationRepository.saveAll(notifications);
    }
    
    public FcmToken saveFCMToken(FcmToken fcmToken) {
        // Delete existing token for this user and platform
        fcmTokenRepository.deleteByUserRoleAndUserIdAndPlatform(
                fcmToken.getUserRole(), fcmToken.getUserId(), fcmToken.getPlatform());
        
        return fcmTokenRepository.save(fcmToken);
    }
}
