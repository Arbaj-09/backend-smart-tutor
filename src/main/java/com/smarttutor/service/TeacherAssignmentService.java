package com.smarttutor.service;

import com.smarttutor.entity.Teacher;
import com.smarttutor.entity.ClassEntity;
import com.smarttutor.entity.Division;
import com.smarttutor.entity.TeacherClassDivision;
import com.smarttutor.repository.TeacherClassDivisionRepository;
import com.smarttutor.repository.TeacherRepository;
import com.smarttutor.repository.ClassRepository;
import com.smarttutor.repository.DivisionRepository;
import com.smarttutor.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TeacherAssignmentService {
    
    @Autowired
    private TeacherClassDivisionRepository assignmentRepository;
    
    @Autowired
    private TeacherRepository teacherRepository;
    
    @Autowired
    private ClassRepository classRepository;
    
    @Autowired
    private DivisionRepository divisionRepository;
    
    public TeacherClassDivision assignTeacherToClassDivision(Long teacherId, Long classId, Long divisionId) {
        Teacher teacher = teacherRepository.findById(teacherId)
            .orElseThrow(() -> new ResourceNotFoundException("Teacher", "id", teacherId));
        
        ClassEntity classEntity = classRepository.findById(classId)
            .orElseThrow(() -> new ResourceNotFoundException("Class", "id", classId));
        
        Division division = divisionRepository.findById(divisionId)
            .orElseThrow(() -> new ResourceNotFoundException("Division", "id", divisionId));
        
        // Check if assignment already exists
        Optional<TeacherClassDivision> existingAssignment = 
            assignmentRepository.findByTeacherIdAndClassIdAndDivisionId(teacherId, classId, divisionId);
        
        if (existingAssignment.isPresent()) {
            return existingAssignment.get();
        }
        
        TeacherClassDivision assignment = new TeacherClassDivision(teacher, classEntity, division);
        return assignmentRepository.save(assignment);
    }
    
    public void removeTeacherAssignment(Long teacherId, Long classId, Long divisionId) {
        Optional<TeacherClassDivision> assignment = 
            assignmentRepository.findByTeacherIdAndClassIdAndDivisionId(teacherId, classId, divisionId);
        
        if (assignment.isPresent()) {
            assignmentRepository.delete(assignment.get());
        }
    }
    
    public List<ClassEntity> getTeacherClasses(Long teacherId) {
        Teacher teacher = teacherRepository.findById(teacherId)
            .orElseThrow(() -> new ResourceNotFoundException("Teacher", "id", teacherId));
        
        return assignmentRepository.findClassesByTeacherId(teacherId);
    }
    
    public List<Division> getTeacherDivisionsByClass(Long teacherId, Long classId) {
        Teacher teacher = teacherRepository.findById(teacherId)
            .orElseThrow(() -> new ResourceNotFoundException("Teacher", "id", teacherId));
        
        ClassEntity classEntity = classRepository.findById(classId)
            .orElseThrow(() -> new ResourceNotFoundException("Class", "id", classId));
        
        return assignmentRepository.findDivisionsByTeacherIdAndClassId(teacherId, classId);
    }
    
    public List<TeacherClassDivision> getTeacherAssignments(Long teacherId) {
        Teacher teacher = teacherRepository.findById(teacherId)
            .orElseThrow(() -> new ResourceNotFoundException("Teacher", "id", teacherId));
        
        return assignmentRepository.findByTeacherId(teacherId);
    }
    
    public List<TeacherClassDivision> assignTeacherToMultipleClasses(Long teacherId, List<ClassDivisionAssignment> assignments) {
        Teacher teacher = teacherRepository.findById(teacherId)
            .orElseThrow(() -> new ResourceNotFoundException("Teacher", "id", teacherId));
        
        List<TeacherClassDivision> results = new java.util.ArrayList<>();
        
        for (ClassDivisionAssignment assignment : assignments) {
            try {
                TeacherClassDivision savedAssignment = assignTeacherToClassDivision(
                    teacherId, assignment.getClassId(), assignment.getDivisionId());
                results.add(savedAssignment);
            } catch (Exception e) {
                // Log error but continue with other assignments
                System.err.println("Failed to assign teacher " + teacherId + " to class " + assignment.getClassId() + " division " + assignment.getDivisionId() + ": " + e.getMessage());
            }
        }
        
        return results;
    }
    
    public static class ClassDivisionAssignment {
        private Long classId;
        private Long divisionId;
        
        public ClassDivisionAssignment() {}
        
        public ClassDivisionAssignment(Long classId, Long divisionId) {
            this.classId = classId;
            this.divisionId = divisionId;
        }
        
        public Long getClassId() { return classId; }
        public void setClassId(Long classId) { this.classId = classId; }
        
        public Long getDivisionId() { return divisionId; }
        public void setDivisionId(Long divisionId) { this.divisionId = divisionId; }
    }
}
