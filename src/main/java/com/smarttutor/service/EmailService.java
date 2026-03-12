package com.smarttutor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendLoginCredentials(String teacherName, String teacherEmail, String loginEmail, String temporaryPassword) {
        SimpleMailMessage message = new SimpleMailMessage();
        
        message.setFrom("SmartTutor <arbajshaikh9561@gmail.com>");
        message.setTo(teacherEmail);
        message.setSubject("SmartTutor Login Credentials");
        
        String emailBody = buildLoginCredentialsEmailBody(teacherName, loginEmail, temporaryPassword);
        message.setText(emailBody);
        
        mailSender.send(message);
    }

    public void sendAccountCreationEmail(String userName, String userEmail, String userMobile) {
        SimpleMailMessage message = new SimpleMailMessage();
        
        message.setFrom("SmartTutor <arbajshaikh9561@gmail.com>");
        message.setTo(userEmail);
        message.setSubject("Welcome to SmartTutor – Account Created");
        
        String emailBody = buildAccountCreationEmailBody(userName);
        message.setText(emailBody);
        
        mailSender.send(message);
    }

    public void sendPasswordResetOTP(String userName, String userEmail, String otpCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        
        message.setFrom("SmartTutor <arbajshaikh9561@gmail.com>");
        message.setTo(userEmail);
        message.setSubject("SmartTutor – Password Reset OTP");
        
        String emailBody = buildPasswordResetOTPEmailBody(userName, otpCode);
        message.setText(emailBody);
        
        mailSender.send(message);
    }

    public void sendPasswordChangeConfirmation(String userName, String userEmail) {
        SimpleMailMessage message = new SimpleMailMessage();
        
        message.setFrom("SmartTutor <arbajshaikh9561@gmail.com>");
        message.setTo(userEmail);
        message.setSubject("SmartTutor – Password Changed Successfully");
        
        String emailBody = buildPasswordChangeConfirmationEmailBody(userName);
        message.setText(emailBody);
        
        mailSender.send(message);
    }

    private String buildLoginCredentialsEmailBody(String teacherName, String loginEmail, String temporaryPassword) {
        return String.format(
            "Dear %s,\n\n" +
            "Welcome to SmartTutor! Your account has been created successfully.\n\n" +
            "Login Details:\n" +
            "Email: %s\n" +
            "Password: Your initial password is your registered mobile number.\n\n" +
            "Please use these credentials to log into the SmartTutor system.\n" +
            "IMPORTANT: Please reset your password immediately after your first login using the 'Forgot Password' option for security.\n\n" +
            "If you have any questions, please contact the administrator.\n\n" +
            "Best regards,\n" +
            "SmartTutor Team",
            teacherName, loginEmail
        );
    }

    private String buildAccountCreationEmailBody(String userName) {
        return String.format(
            "Dear %s,\n\n" +
            "Welcome to SmartTutor!\n\n" +
            "Your account has been created successfully.\n\n" +
            "Login Information:\n" +
            "Email: Your registered email address\n" +
            "Password: Your initial password is your registered mobile number.\n\n" +
            "Important Security Notice:\n" +
            "Please reset your password immediately after first login using the 'Forgot Password' option.\n\n" +
            "If you have any questions or need assistance, please contact the administrator.\n\n" +
            "Best regards,\n" +
            "SmartTutor Team\n" +
            "Smart Learning Platform",
            userName
        );
    }

    private String buildPasswordResetOTPEmailBody(String userName, String otpCode) {
        return String.format(
            "Dear %s,\n\n" +
            "You have requested to reset your SmartTutor password.\n\n" +
            "Your One Time Password (OTP) is: %s\n\n" +
            "Important Information:\n" +
            "- This OTP is valid for 5 minutes only\n" +
            "- This OTP can be used only once\n" +
            "- Please enter this OTP to proceed with password reset\n\n" +
            "Security Warning:\n" +
            "- Never share your OTP with anyone\n" +
            "- SmartTutor staff will never ask for your OTP\n" +
            "- If you did not request this password reset, please contact administrator immediately\n\n" +
            "Best regards,\n" +
            "SmartTutor Team",
            userName, otpCode
        );
    }

    private String buildPasswordChangeConfirmationEmailBody(String userName) {
        return String.format(
            "Dear %s,\n\n" +
            "Your SmartTutor password has been changed successfully.\n\n" +
            "Change Details:\n" +
            "Date: %s\n" +
            "Time: %s\n\n" +
            "Security Notice:\n" +
            "If you did not initiate this password change, please contact the administrator immediately.\n\n" +
            "For your account security, please ensure:\n" +
            "- Your password is strong and unique\n" +
            "- Never share your password with anyone\n" +
            "- Change your password regularly\n\n" +
            "Best regards,\n" +
            "SmartTutor Team",
            userName,
            java.time.LocalDate.now().toString(),
            java.time.LocalTime.now().toString()
        );
    }
}
