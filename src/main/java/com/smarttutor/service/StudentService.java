package com.smarttutor.service;

import com.smarttutor.dto.StudentDTO;
import com.smarttutor.entity.Student;
import com.smarttutor.entity.ClassEntity;
import com.smarttutor.entity.Division;
import com.smarttutor.entity.Teacher;
import com.smarttutor.exception.ResourceNotFoundException;
import com.smarttutor.repository.StudentRepository;
import com.smarttutor.repository.ClassRepository;
import com.smarttutor.repository.DivisionRepository;
import com.smarttutor.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

@Service
public class StudentService {
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private ClassRepository classRepository;
    
    @Autowired
    private DivisionRepository divisionRepository;
    
    @Autowired
    private TeacherRepository teacherRepository;
    
    public Student createStudent(StudentDTO studentDTO) {
        if (studentRepository.existsByEmail(studentDTO.getEmail())) {
            throw new IllegalArgumentException("Student with email " + studentDTO.getEmail() + " already exists");
        }
        
        // Validate class and division
        ClassEntity classEntity = classRepository.findById(studentDTO.getClassId())
                .orElseThrow(() -> new ResourceNotFoundException("Class", "id", studentDTO.getClassId()));
        
        Division division = divisionRepository.findById(studentDTO.getDivisionId())
                .orElseThrow(() -> new ResourceNotFoundException("Division", "id", studentDTO.getDivisionId()));
        
        // Get teacher for this class and division
        Teacher teacher = teacherRepository.findActiveTeacherByClassAndDivision(
                studentDTO.getClassId(), studentDTO.getDivisionId())
                .orElse(null);
        
        Student student = new Student();
        student.setName(studentDTO.getName());
        student.setRollNo(studentDTO.getRollNo());
        student.setEmail(studentDTO.getEmail());
        student.setPassword(hashPassword(studentDTO.getPassword()));
        student.setClassEntity(classEntity);
        student.setDivision(division);
        student.setTeacher(teacher);
        
        return studentRepository.save(student);
    }
    
    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));
    }
    
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
    
    public List<Student> getStudentsByClassAndDivision(Long classId, Long divisionId) {
        return studentRepository.findActiveStudentsByClassAndDivision(classId, divisionId);
    }
    
    public Student updateStudent(Long id, StudentDTO studentDTO) {
        Student existingStudent = getStudentById(id);
        
        existingStudent.setName(studentDTO.getName());
        existingStudent.setRollNo(studentDTO.getRollNo());
        existingStudent.setActive(studentDTO.getActive());
        
        if (studentDTO.getEmail() != null && !studentDTO.getEmail().equals(existingStudent.getEmail())) {
            if (studentRepository.existsByEmail(studentDTO.getEmail())) {
                throw new IllegalArgumentException("Student with email " + studentDTO.getEmail() + " already exists");
            }
            existingStudent.setEmail(studentDTO.getEmail());
        }
        
        // Update class and division if provided
        if (studentDTO.getClassId() != null) {
            ClassEntity classEntity = classRepository.findById(studentDTO.getClassId())
                    .orElseThrow(() -> new ResourceNotFoundException("Class", "id", studentDTO.getClassId()));
            existingStudent.setClassEntity(classEntity);
        }
        
        if (studentDTO.getDivisionId() != null) {
            Division division = divisionRepository.findById(studentDTO.getDivisionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Division", "id", studentDTO.getDivisionId()));
            existingStudent.setDivision(division);
            
            // Update teacher assignment
            Teacher teacher = teacherRepository.findActiveTeacherByClassAndDivision(
                    studentDTO.getClassId(), studentDTO.getDivisionId())
                    .orElse(null);
            existingStudent.setTeacher(teacher);
        }
        
        // Update password if provided
        if (studentDTO.getPassword() != null && !studentDTO.getPassword().trim().isEmpty()) {
            existingStudent.setPassword(hashPassword(studentDTO.getPassword()));
        }
        
        return studentRepository.save(existingStudent);
    }
    
    public void deleteStudent(Long id) {
        Student student = getStudentById(id);
        studentRepository.delete(student);
    }
    
    public List<Student> getStudentsByTeacher(Long teacherId) {
        return studentRepository.findByTeacherId(teacherId);
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
