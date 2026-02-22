package com.smarttutor.repository;

import com.smarttutor.entity.Notification;
import com.smarttutor.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    
    @Query("SELECT n FROM Notification n WHERE n.recipientRole = :role AND n.recipientId = :userId ORDER BY n.createdAt DESC")
    List<Notification> findByRecipientRoleAndUserId(@Param("role") Role role, @Param("userId") Long userId);
    
    @Query("SELECT n FROM Notification n WHERE n.recipientRole = :role ORDER BY n.createdAt DESC")
    List<Notification> findByRecipientRole(@Param("role") Role role);
    
    @Query("SELECT n FROM Notification n WHERE n.recipientRole = :role AND n.recipientId = :userId AND n.isRead = false ORDER BY n.createdAt DESC")
    List<Notification> findUnreadByRecipientRoleAndUserId(@Param("role") Role role, @Param("userId") Long userId);
    
    @Query("SELECT COUNT(n) FROM Notification n WHERE n.recipientRole = :role AND n.recipientId = :userId AND n.isRead = false")
    Long countUnreadByRecipientRoleAndUserId(@Param("role") Role role, @Param("userId") Long userId);
}
