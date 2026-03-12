package com.smarttutor.mapper;

import com.smarttutor.dto.response.StudentResponseDTO;
import com.smarttutor.entity.Student;
import com.smarttutor.entity.Teacher;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class StudentMapper {
    
    public static StudentResponseDTO toDTO(Student student) {
        if (student == null) return null;
        
        StudentResponseDTO dto = new StudentResponseDTO();
        dto.setId(student.getId());
        dto.setName(student.getName());
        dto.setEmail(student.getEmail());
        dto.setPhone(student.getPhone());
        dto.setRollNo(student.getRollNo());
        dto.setActive(student.getActive());
        dto.setCreatedAt(student.getCreatedAt());
        
        // Map class and division info
        try {
            if (student.getClassEntity() != null) {
                dto.setClassName(student.getClassEntity().getClassName());
            }
        } catch (Exception e) {
            dto.setClassName("Unknown Class");
        }
        
        try {
            if (student.getDivision() != null) {
                dto.setDivisionName(student.getDivision().getDivisionName());
            }
        } catch (Exception e) {
            dto.setDivisionName("Unknown Division");
        }
        
        // For now, set single teacher name (we'll enhance this later)
        try {
            if (student.getTeacher() != null) {
                dto.setTeacherName(student.getTeacher().getName());
            } else {
                dto.setTeacherName("Not Assigned");
            }
        } catch (Exception e) {
            dto.setTeacherName("Not Assigned");
        }
        
        return dto;
    }
    
    public static StudentResponseDTO toDTOWithAllTeachers(Student student, List<Teacher> assignedTeachers) {
        if (student == null) return null;
        
        StudentResponseDTO dto = toDTO(student);
        
        // Set all assigned teachers with their subjects
        if (assignedTeachers != null && !assignedTeachers.isEmpty()) {
            List<String> teacherNames = assignedTeachers.stream()
                .map(teacher -> teacher.getName() + " (" + (teacher.getSubject() != null ? teacher.getSubject() : "No Subject") + ")")
                .collect(Collectors.toList());
            dto.setTeacherName(String.join(", ", teacherNames));
        }
        
        return dto;
    }
    
    public static List<StudentResponseDTO> toDTOList(List<Student> students) {
        if (students == null) return new ArrayList<>();
        
        return students.stream()
            .map(StudentMapper::toDTO)
            .collect(Collectors.toList());
    }
}
