package com.smarttutor.service;

import com.smarttutor.entity.Notes;
import com.smarttutor.entity.Student;
import com.smarttutor.entity.Teacher;
import com.smarttutor.entity.ClassEntity;
import com.smarttutor.entity.Division;
import com.smarttutor.repository.NotesRepository;
import com.smarttutor.repository.StudentRepository;
import com.smarttutor.repository.ClassRepository;
import com.smarttutor.repository.DivisionRepository;
import com.smarttutor.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
@Transactional
public class NotesService {
    
    @Autowired
    private NotesRepository notesRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private FirebaseService firebaseService;
    
    @Autowired
    private TeacherService teacherService;
    
    @Autowired
    private ClassRepository classRepository;
    
    @Autowired
    private DivisionRepository divisionRepository;
    
    public Notes uploadNotes(NotesRequest request, MultipartFile file, Long teacherId) {
        try {
            // Validate teacher exists
            Teacher teacher = teacherService.getTeacherById(teacherId);
            
            // Get class and division entities
            ClassEntity classEntity = classRepository.findById(request.getClassId())
                .orElseThrow(() -> new ResourceNotFoundException("Class", "id", request.getClassId()));
            
            Division division = divisionRepository.findById(request.getDivisionId())
                .orElseThrow(() -> new ResourceNotFoundException("Division", "id", request.getDivisionId()));
            
            // Create notes entity
            Notes notes = new Notes();
            notes.setTitle(request.getTitle());
            notes.setSubject(request.getSubject());
            notes.setDescription(request.getDescription());
            notes.setFileName(file.getOriginalFilename());
            notes.setFilePath(uploadFile(file, request.getSubject())); // Pass subject for folder creation
            notes.setFileType(getFileType(file.getOriginalFilename()));
            notes.setFileSize(file.getSize());
            notes.setClassEntity(classEntity);
            notes.setDivision(division);
            notes.setTeacher(teacher);
            notes.setUploadedAt(LocalDateTime.now());
            notes.setActive(true);
            
            // Save notes
            Notes savedNotes = notesRepository.save(notes);
            
            // Send FCM notifications to students
            sendNotificationsToStudents(savedNotes, teacher);
            
            return savedNotes;
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload notes: " + e.getMessage(), e);
        }
    }
    
    private void sendNotificationsToStudents(Notes notes, Teacher teacher) {
        try {
            // Get students for the specific class and division
            List<Student> students = studentRepository.findActiveStudentsByClassAndDivision(
                notes.getClassId(), notes.getDivisionId());
            
            // Filter students who have FCM tokens
            List<Student> studentsWithTokens = new ArrayList<>();
            for (Student student : students) {
                if (student.getFcmToken() != null && !student.getFcmToken().trim().isEmpty()) {
                    studentsWithTokens.add(student);
                }
            }
            
            // Send notifications
            for (Student student : studentsWithTokens) {
                String title = "New Notes Uploaded";
                String body = "New notes uploaded by " + teacher.getName() + " for " + notes.getSubject();
                
                firebaseService.sendNotification(
                    student.getFcmToken(),
                    title,
                    body,
                    Map.of(
                        "type", "notes",
                        "notesId", notes.getId().toString(),
                        "subject", notes.getSubject(),
                        "teacherName", teacher.getName()
                    )
                );
            }
            
            System.out.println("FCM notifications sent to " + studentsWithTokens.size() + " students");
            
        } catch (Exception e) {
            System.err.println("Failed to send FCM notifications: " + e.getMessage());
            // Don't fail the upload if notifications fail
        }
    }
    
    private String uploadFile(MultipartFile file, String subject) {
        try {
            // Create subject-wise upload directory
            String uploadDir = "uploads/" + subject.replaceAll("[^a-zA-Z0-9.-]", "_");
            File dir = new File(uploadDir);
            
            // Create directory if it doesn't exist
            if (!dir.exists()) {
                dir.mkdirs();
                System.out.println("📁 Created directory: " + dir.getAbsolutePath());
            }
            
            // Generate unique filename with timestamp
            String timestamp = String.valueOf(System.currentTimeMillis());
            String originalFilename = file.getOriginalFilename();
            String fileName = timestamp + "_" + originalFilename;
            
            // Create full file path
            Path filePath = Paths.get(uploadDir, fileName);
            
            // Copy file to the target location
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            
            // Return relative path for database storage
            String relativePath = "uploads/" + subject.replaceAll("[^a-zA-Z0-9.-]", "_") + "/" + fileName;
            System.out.println("📄 File uploaded successfully: " + relativePath);
            
            return relativePath;
            
        } catch (Exception e) {
            System.err.println("❌ Error uploading file: " + e.getMessage());
            throw new RuntimeException("Failed to upload file: " + e.getMessage(), e);
        }
    }
    
    private String getFileType(String fileName) {
        if (fileName == null) return "unknown";
        
        String extension = fileName.toLowerCase();
        if (extension.endsWith(".pdf")) return "pdf";
        if (extension.endsWith(".jpg") || extension.endsWith(".jpeg")) return "image";
        if (extension.endsWith(".png")) return "image";
        if (extension.endsWith(".doc") || extension.endsWith(".docx")) return "document";
        if (extension.endsWith(".ppt") || extension.endsWith(".pptx")) return "presentation";
        
        return "other";
    }
    
    public List<Notes> getNotesByClassAndDivision(Long classId, Long divisionId) {
        return notesRepository.findByClassIdAndDivisionIdAndActiveOrderByUploadedAtDesc(classId, divisionId, true);
    }
    
    public List<Notes> getNotesByStudent(Long studentId) {
        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new ResourceNotFoundException("Student", "id", studentId));
        
        return notesRepository.findByClassIdAndDivisionIdAndActiveOrderByUploadedAtDesc(
            student.getClassEntity().getId(), 
            student.getDivision().getId(), 
            true
        );
    }
    
    public Notes getNotesById(Long notesId) {
        return notesRepository.findById(notesId)
            .orElseThrow(() -> new ResourceNotFoundException("Notes", "id", notesId));
    }
    
    public void deleteNotes(Long notesId, Long teacherId) {
        Notes notes = getNotesById(notesId);
        
        // Verify teacher owns this notes
        if (!notes.getTeacher().getId().equals(teacherId)) {
            throw new IllegalArgumentException("Teacher can only delete their own notes");
        }
        
        notes.setActive(false);
        notesRepository.save(notes);
    }
    
    public static class NotesRequest {
        private String title;
        private String subject;
        private String description;
        private Long classId;
        private Long divisionId;
        
        // Constructors
        public NotesRequest() {}
        
        // Getters and Setters
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        
        public String getSubject() { return subject; }
        public void setSubject(String subject) { this.subject = subject; }
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        
        public Long getClassId() { return classId; }
        public void setClassId(Long classId) { this.classId = classId; }
        
        public Long getDivisionId() { return divisionId; }
        public void setDivisionId(Long divisionId) { this.divisionId = divisionId; }
    }
}
