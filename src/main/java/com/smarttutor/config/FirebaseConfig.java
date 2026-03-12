package com.smarttutor.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {
    
    @Value("${firebase.service.account.file:firebase-service-account.json}")
    private String firebaseServiceAccountFile;
    
    @PostConstruct
    public void init() {
        try {
            if (FirebaseApp.getApps().isEmpty()) {
                InputStream serviceAccount = 
                    new ClassPathResource(firebaseServiceAccountFile).getInputStream();
                
                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .build();

                FirebaseApp.initializeApp(options);
                System.out.println("🔥 Firebase initialized successfully");
            }
        } catch (Exception e) {
            System.out.println("❌ Firebase initialization failed: " + e.getMessage());
            System.out.println("🔧 FCM notifications will be disabled until Firebase is properly configured");
        }
    }
    
    @Bean
    public FirebaseMessaging firebaseMessaging() {
        try {
            return FirebaseMessaging.getInstance();
        } catch (Exception e) {
            System.out.println("❌ FirebaseMessaging bean creation failed: " + e.getMessage());
            return null;
        }
    }
}
