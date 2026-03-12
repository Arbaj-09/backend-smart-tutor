package com.smarttutor.service;

import com.smarttutor.entity.FcmToken;
import com.smarttutor.entity.Notification;
import com.smarttutor.entity.Student;
import com.smarttutor.entity.Hod;
import com.smarttutor.entity.ClassEntity;
import com.smarttutor.entity.Division;
import com.smarttutor.repository.FcmTokenRepository;
import com.smarttutor.repository.NotificationRepository;
import com.smarttutor.repository.StudentRepository;
import com.smarttutor.repository.HodRepository;
import com.smarttutor.repository.ClassRepository;
import com.smarttutor.repository.DivisionRepository;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class NotificationService {
    
    @Autowired
    private NotificationRepository notificationRepository;
    
    @Autowired
    private FcmTokenRepository fcmTokenRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private HodRepository hodRepository;
    
    @Autowired
    private ClassRepository classRepository;
    
    @Autowired
    private DivisionRepository divisionRepository;
    
    @Autowired(required = false)
    private FirebaseMessaging firebaseMessaging;
    
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
    
    @Transactional
    public FcmToken saveFCMToken(FcmToken fcmToken) {
        // Delete existing token for this user and platform
        fcmTokenRepository.deleteByUserRoleAndUserIdAndPlatform(
                fcmToken.getUserRole(), fcmToken.getUserId(), fcmToken.getPlatform());
        
        return fcmTokenRepository.save(fcmToken);
    }
    
    public void sendNoteUploadNotification(Long classId, Long divisionId, String noteTitle, String teacherName, String subject) {
        String message = noteTitle + " uploaded by " + teacherName;
        System.out.println("🔔 Sending note upload notification for class: " + classId + ", division: " + divisionId);

        // Get class and division names
        String className = "Unknown Class";
        String divisionName = "Unknown Division";
        
        try {
            // Get class name
            ClassEntity classEntity = classRepository.findById(classId).orElse(null);
            if (classEntity != null) {
                className = classEntity.getClassName();
            }
            
            // Get division name  
            Division division = divisionRepository.findById(divisionId).orElse(null);
            if (division != null) {
                divisionName = division.getDivisionName();
            }
        } catch (Exception e) {
            System.out.println("⚠️ Could not fetch class/division names: " + e.getMessage());
        }

        // 1️⃣ Notify Students
        List<Student> students = studentRepository
                .findByClassEntityIdAndDivisionId(classId, divisionId);

        System.out.println("👨‍🎓 Found " + students.size() + " students to notify");

        for (Student student : students) {
            // Save notification with details
            Notification n = new Notification(
                    com.smarttutor.enums.Role.STUDENT,
                    student.getId(),
                    "New Notes Uploaded",
                    message
            );
            
            // Set additional details
            n.setTeacherName(teacherName);
            n.setSubject(subject);
            n.setClassName(className);
            n.setDivisionName(divisionName);

            notificationRepository.save(n);
            System.out.println("💾 Saved notification for student: " + student.getName());

            sendPush(student.getId(), com.smarttutor.enums.Role.STUDENT, message);
        }

        // 2️⃣ Notify HOD
        List<Hod> hods = hodRepository.findAll();

        System.out.println("👨‍💼 Found " + hods.size() + " HODs to notify");

        for (Hod hod : hods) {
            Notification n = new Notification(
                    com.smarttutor.enums.Role.HOD,
                    hod.getId(),
                    "Teacher Uploaded Notes",
                    message
            );
            
            // Set additional details
            n.setTeacherName(teacherName);
            n.setSubject(subject);
            n.setClassName(className);
            n.setDivisionName(divisionName);

            notificationRepository.save(n);
            System.out.println("💾 Saved notification for HOD: " + hod.getName());

            sendPush(hod.getId(), com.smarttutor.enums.Role.HOD, message);
        }

        System.out.println("✅ Note upload notification process completed");
    }

    private void sendPush(Long userId, com.smarttutor.enums.Role role, String message) {
        // Check if FirebaseMessaging is available
        if (firebaseMessaging == null) {
            System.out.println("⚠️ FirebaseMessaging not available - skipping push notification for user " + userId);
            return;
        }
        
        List<FcmToken> tokens =
                fcmTokenRepository.findByUserIdAndUserRole(userId, role);

        System.out.println("📱 Found " + tokens.size() + " FCM tokens for user " + userId + " with role " + role);

        for (FcmToken token : tokens) {
            Message msg = Message.builder()
                    .setToken(token.getToken())
                    .setNotification(
                        com.google.firebase.messaging.Notification.builder()
                            .setTitle("SmartTutor")
                            .setBody(message)
                            .build()
                    )
                    .putData("type", "NOTE_UPLOAD")
                    .putData("screen", "notes")
                    .build();

            try {
                String response = firebaseMessaging.send(msg);
                System.out.println("✅ FCM sent successfully: " + response);
            } catch (Exception e) {
                System.out.println("❌ FCM error: " + e.getMessage());
            }
        }
    }
}
