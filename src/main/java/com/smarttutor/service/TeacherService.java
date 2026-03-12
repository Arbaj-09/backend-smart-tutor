package com.smarttutor.service;

import com.smarttutor.entity.Teacher;
import com.smarttutor.entity.ClassEntity;
import com.smarttutor.entity.Division;
import com.smarttutor.entity.TeacherClassDivision;
import com.smarttutor.exception.ResourceNotFoundException;
import com.smarttutor.repository.TeacherRepository;
import com.smarttutor.repository.ClassRepository;
import com.smarttutor.repository.DivisionRepository;
import com.smarttutor.repository.TeacherClassDivisionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TeacherService {
    
    @Autowired
    private TeacherRepository teacherRepository;
    
    @Autowired
    private ClassRepository classRepository;
    
    @Autowired
    private DivisionRepository divisionRepository;
    
    @Autowired
    private TeacherClassDivisionRepository teacherClassDivisionRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public Teacher createTeacher(Teacher teacher) {
        if (teacherRepository.existsByEmail(teacher.getEmail())) {
            throw new IllegalArgumentException("Teacher with email " + teacher.getEmail() + " already exists");
        }
        
        // Hash password
        teacher.setPassword(passwordEncoder.encode(teacher.getPassword()));
        
        // Set created timestamp
        teacher.setCreatedAt(LocalDateTime.now());
        
        return teacherRepository.save(teacher);
    }
    
    public Teacher getTeacherById(Long id) {
        return teacherRepository.findByIdWithAssignments(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher", "id", id));
    }
    
    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAllWithAssignments();
    }
    
    public Teacher updateTeacher(Long id, Teacher teacher) {
        Teacher existingTeacher = getTeacherById(id);
        
        existingTeacher.setName(teacher.getName());
        existingTeacher.setEmail(teacher.getEmail());
        existingTeacher.setPhone(teacher.getPhone());
        existingTeacher.setSubject(teacher.getSubject());
        existingTeacher.setActive(teacher.getActive() != null ? teacher.getActive() : true);
        existingTeacher.setFcmToken(teacher.getFcmToken());
        
        // Update password if provided
        if (teacher.getPassword() != null && !teacher.getPassword().trim().isEmpty()) {
            existingTeacher.setPassword(passwordEncoder.encode(teacher.getPassword()));
        }
        
        return teacherRepository.save(existingTeacher);
    }
    
    public void deleteTeacher(Long id) {
        Teacher teacher = getTeacherById(id);
        teacherRepository.delete(teacher);
    }
    
    public void clearTeacherAssignments(Long teacherId) {
        List<TeacherClassDivision> assignments = teacherClassDivisionRepository.findByTeacherId(teacherId);
        teacherClassDivisionRepository.deleteAll(assignments);
    }
    
    public void assignTeacherToClassDivision(Teacher teacher, ClassEntity classEntity, Division division) {
        TeacherClassDivision assignment = new TeacherClassDivision();
        assignment.setTeacher(teacher);
        assignment.setClassEntity(classEntity);
        assignment.setDivision(division);
        teacherClassDivisionRepository.save(assignment);
    }
}
