package com.smarttutor.service;

import com.smarttutor.entity.PasswordResetOTP;
import com.smarttutor.entity.Hod;
import com.smarttutor.entity.Teacher;
import com.smarttutor.entity.Student;
import com.smarttutor.enums.Role;
import com.smarttutor.repository.PasswordResetOTPRepository;
import com.smarttutor.repository.HodRepository;
import com.smarttutor.repository.TeacherRepository;
import com.smarttutor.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PasswordResetService {
    
    @Autowired
    private PasswordResetOTPRepository otpRepository;
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private HodRepository hodRepository;
    
    @Autowired
    private TeacherRepository teacherRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    private static final int OTP_LENGTH = 6;
    private static final int OTP_EXPIRY_MINUTES = 5;
    
    public String generateAndSendOTP(String email) throws Exception {
        // Check if email exists and determine role
        UserRoleInfo userRoleInfo = findUserByEmail(email);
        if (userRoleInfo == null) {
            throw new Exception("Email not found in our system");
        }
        
        // Delete any existing OTP for this email
        otpRepository.deleteByEmail(email);
        
        // Generate 6-digit OTP
        String otpCode = generateOTP();
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES);
        
        // Save new OTP
        PasswordResetOTP otp = new PasswordResetOTP(email, userRoleInfo.role, otpCode, expiresAt);
        otpRepository.save(otp);
        
        // Send OTP email
        emailService.sendPasswordResetOTP(userRoleInfo.name, email, otpCode);
        
        return "OTP sent successfully to " + email;
    }
    
    public String resetPassword(String email, String otpCode, String newPassword, String confirmPassword) throws Exception {
        // Validate passwords
        if (!newPassword.equals(confirmPassword)) {
            throw new Exception("Passwords do not match");
        }
        
        if (newPassword.length() < 6) {
            throw new Exception("Password must be at least 6 characters long");
        }
        
        // Find and validate OTP
        Optional<PasswordResetOTP> otpOpt = otpRepository.findByEmailAndOtpCodeAndIsUsedFalse(email, otpCode);
        if (otpOpt.isEmpty()) {
            throw new Exception("Invalid OTP");
        }
        
        PasswordResetOTP otp = otpOpt.get();
        if (otp.isExpired()) {
            throw new Exception("OTP has expired");
        }
        
        // Find user and update password
        UserRoleInfo userRoleInfo = findUserByEmail(email);
        String encodedPassword = passwordEncoder.encode(newPassword);
        
        switch (userRoleInfo.role) {
            case HOD:
                Hod hod = hodRepository.findByEmail(email).orElseThrow(() -> new Exception("HOD not found"));
                hod.setPassword(encodedPassword);
                hodRepository.save(hod);
                break;
                
            case TEACHER:
                Teacher teacher = teacherRepository.findByEmail(email).orElseThrow(() -> new Exception("Teacher not found"));
                teacher.setPassword(encodedPassword);
                teacherRepository.save(teacher);
                break;
                
            case STUDENT:
                Student student = studentRepository.findByEmail(email).orElseThrow(() -> new Exception("Student not found"));
                student.setPassword(encodedPassword);
                studentRepository.save(student);
                break;
        }
        
        // Mark OTP as used
        otp.setUsed(true);
        otpRepository.save(otp);
        
        // Send confirmation email
        emailService.sendPasswordChangeConfirmation(userRoleInfo.name, email);
        
        return "Password reset successfully";
    }
    
    private String generateOTP() {
        SecureRandom random = new SecureRandom();
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }
    
    private UserRoleInfo findUserByEmail(String email) {
        // Check HOD
        Optional<Hod> hod = hodRepository.findByEmail(email);
        if (hod.isPresent()) {
            return new UserRoleInfo(hod.get().getName(), Role.HOD);
        }
        
        // Check Teacher
        Optional<Teacher> teacher = teacherRepository.findByEmail(email);
        if (teacher.isPresent()) {
            return new UserRoleInfo(teacher.get().getName(), Role.TEACHER);
        }
        
        // Check Student
        Optional<Student> student = studentRepository.findByEmail(email);
        if (student.isPresent()) {
            return new UserRoleInfo(student.get().getName(), Role.STUDENT);
        }
        
        return null;
    }
    
    private static class UserRoleInfo {
        String name;
        Role role;
        
        public UserRoleInfo(String name, Role role) {
            this.name = name;
            this.role = role;
        }
    }
}
