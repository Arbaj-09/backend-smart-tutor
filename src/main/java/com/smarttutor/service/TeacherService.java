package com.smarttutor.service;

import com.smarttutor.entity.Teacher;
import com.smarttutor.entity.ClassEntity;
import com.smarttutor.entity.Division;
import com.smarttutor.exception.ResourceNotFoundException;
import com.smarttutor.repository.TeacherRepository;
import com.smarttutor.repository.ClassRepository;
import com.smarttutor.repository.DivisionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

@Service
public class TeacherService {
    
    @Autowired
    private TeacherRepository teacherRepository;
    
    @Autowired
    private ClassRepository classRepository;
    
    @Autowired
    private DivisionRepository divisionRepository;
    
    public Teacher createTeacher(Teacher teacher) {
        if (teacherRepository.existsByEmail(teacher.getEmail())) {
            throw new IllegalArgumentException("Teacher with email " + teacher.getEmail() + " already exists");
        }
        
        // Validate class and division if provided
        if (teacher.getClassEntity() != null) {
            ClassEntity classEntity = classRepository.findById(teacher.getClassEntity().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Class", "id", teacher.getClassEntity().getId()));
            teacher.setClassEntity(classEntity);
        }
        
        if (teacher.getDivision() != null) {
            Division division = divisionRepository.findById(teacher.getDivision().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Division", "id", teacher.getDivision().getId()));
            teacher.setDivision(division);
        }
        
        // Hash password
        teacher.setPassword(hashPassword(teacher.getPassword()));
        
        return teacherRepository.save(teacher);
    }
    
    public Teacher getTeacherById(Long id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher", "id", id));
    }
    
    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }
    
    public Teacher updateTeacher(Long id, Teacher teacher) {
        Teacher existingTeacher = getTeacherById(id);
        
        existingTeacher.setName(teacher.getName());
        existingTeacher.setEmail(teacher.getEmail());
        existingTeacher.setActive(teacher.getActive());
        
        // Update class and division if provided
        if (teacher.getClassEntity() != null) {
            ClassEntity classEntity = classRepository.findById(teacher.getClassEntity().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Class", "id", teacher.getClassEntity().getId()));
            existingTeacher.setClassEntity(classEntity);
        }
        
        if (teacher.getDivision() != null) {
            Division division = divisionRepository.findById(teacher.getDivision().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Division", "id", teacher.getDivision().getId()));
            existingTeacher.setDivision(division);
        }
        
        // Update password if provided
        if (teacher.getPassword() != null && !teacher.getPassword().trim().isEmpty()) {
            existingTeacher.setPassword(hashPassword(teacher.getPassword()));
        }
        
        return teacherRepository.save(existingTeacher);
    }
    
    public void deleteTeacher(Long id) {
        Teacher teacher = getTeacherById(id);
        teacherRepository.delete(teacher);
    }
    
    public Teacher getTeacherByClassAndDivision(Long classId, Long divisionId) {
        return teacherRepository.findActiveTeacherByClassAndDivision(classId, divisionId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher", "class and division", classId + "," + divisionId));
    }
    
    public List<Teacher> getTeachersByClass(Long classId) {
        return teacherRepository.findByClassEntityId(classId);
    }

    // Simple password hashing (SHA-256)
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
}
