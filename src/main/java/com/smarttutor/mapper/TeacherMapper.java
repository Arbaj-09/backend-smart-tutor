package com.smarttutor.mapper;

import com.smarttutor.dto.response.TeacherResponseDTO;
import com.smarttutor.dto.response.ClassDivisionDTO;
import com.smarttutor.entity.Teacher;
import com.smarttutor.entity.TeacherClassDivision;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TeacherMapper {
    
    public static TeacherResponseDTO toDTO(Teacher teacher) {
        if (teacher == null) return null;
        
        TeacherResponseDTO dto = new TeacherResponseDTO();
        dto.setId(teacher.getId());
        dto.setName(teacher.getName());
        dto.setEmail(teacher.getEmail());
        dto.setPhone(teacher.getPhone());
        dto.setSubject(teacher.getSubject());
        dto.setActive(teacher.getActive());
        dto.setCreatedAt(teacher.getCreatedAt());
        
        // Map class assignments - handle lazy loading safely
        try {
            if (teacher.getAssignments() != null) {
                List<ClassDivisionDTO> assignments = teacher.getAssignments().stream()
                    .map(TeacherMapper::toClassDivisionDTO)
                    .collect(Collectors.toList());
                dto.setAssignedClasses(assignments);
            }
        } catch (Exception e) {
            // Lazy loading failed, set empty list
            dto.setAssignedClasses(new ArrayList<>());
        }
        
        return dto;
    }
    
    public static ClassDivisionDTO toClassDivisionDTO(TeacherClassDivision assignment) {
        if (assignment == null) return null;
        
        ClassDivisionDTO dto = new ClassDivisionDTO();
        
        try {
            if (assignment.getClassEntity() != null) {
                dto.setClassId(assignment.getClassEntity().getId());
                dto.setClassName(assignment.getClassEntity().getClassName());
            }
        } catch (Exception e) {
            // Lazy loading failed, set default values
            dto.setClassId(null);
            dto.setClassName("Unknown");
        }
        
        try {
            if (assignment.getDivision() != null) {
                dto.setDivisionId(assignment.getDivision().getId());
                dto.setDivisionName(assignment.getDivision().getDivisionName());
            }
        } catch (Exception e) {
            // Lazy loading failed, set default values
            dto.setDivisionId(null);
            dto.setDivisionName("Unknown");
        }
        
        return dto;
    }
    
    public static List<TeacherResponseDTO> toDTOList(List<Teacher> teachers) {
        if (teachers == null) return new ArrayList<>();
        
        return teachers.stream()
            .map(TeacherMapper::toDTO)
            .collect(Collectors.toList());
    }
}
