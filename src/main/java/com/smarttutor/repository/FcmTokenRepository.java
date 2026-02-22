package com.smarttutor.repository;

import com.smarttutor.entity.FcmToken;
import com.smarttutor.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FcmTokenRepository extends JpaRepository<FcmToken, Long> {
    
    @Query("SELECT ft FROM FcmToken ft WHERE ft.userRole = :role AND ft.userId = :userId AND ft.platform = 'WEB'")
    Optional<FcmToken> findByUserRoleAndUserIdAndPlatform(@Param("role") Role role, @Param("userId") Long userId);
    
    @Query("SELECT ft FROM FcmToken ft WHERE ft.userRole = :role AND ft.userId = :userId")
    List<FcmToken> findByUserRoleAndUserId(@Param("role") Role role, @Param("userId") Long userId);
    
    @Query("SELECT ft FROM FcmToken ft WHERE ft.userRole = :role")
    List<FcmToken> findByUserRole(@Param("role") Role role);
    
    void deleteByUserRoleAndUserIdAndPlatform(Role role, Long userId, FcmToken.Platform platform);
}
