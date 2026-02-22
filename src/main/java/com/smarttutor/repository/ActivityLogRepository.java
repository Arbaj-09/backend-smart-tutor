package com.smarttutor.repository;

import com.smarttutor.entity.ActivityLog;
import com.smarttutor.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {
    
    @Query("SELECT al FROM ActivityLog al WHERE al.userRole = :role AND al.userId = :userId ORDER BY al.createdAt DESC")
    List<ActivityLog> findByUserRoleAndUserId(@Param("role") Role role, @Param("userId") Long userId);
    
    @Query("SELECT al FROM ActivityLog al WHERE al.userRole = :role ORDER BY al.createdAt DESC")
    List<ActivityLog> findByUserRole(@Param("role") Role role);
    
    @Query("SELECT al FROM ActivityLog al WHERE al.action = :action ORDER BY al.createdAt DESC")
    List<ActivityLog> findByAction(@Param("action") String action);
    
    @Query("SELECT al FROM ActivityLog al WHERE al.createdAt BETWEEN :startDate AND :endDate ORDER BY al.createdAt DESC")
    List<ActivityLog> findByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT al FROM ActivityLog al WHERE al.userRole = :role AND al.createdAt BETWEEN :startDate AND :endDate ORDER BY al.createdAt DESC")
    List<ActivityLog> findByUserRoleAndDateRange(@Param("role") Role role, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
