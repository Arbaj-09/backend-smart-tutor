package com.smarttutor.repository;

import com.smarttutor.entity.PasswordResetOTP;
import com.smarttutor.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetOTPRepository extends JpaRepository<PasswordResetOTP, Long> {
    
    Optional<PasswordResetOTP> findByEmailAndIsUsedFalse(String email);
    
    Optional<PasswordResetOTP> findByEmailAndOtpCodeAndIsUsedFalse(String email, String otpCode);
    
    void deleteByEmail(String email);
}
