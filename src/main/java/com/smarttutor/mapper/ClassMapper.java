package com.smarttutor.mapper;

import com.smarttutor.dto.response.ClassResponseDTO;
import com.smarttutor.dto.response.DivisionResponseDTO;
import com.smarttutor.entity.ClassEntity;
import com.smarttutor.repository.ClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ClassMapper {
    
    private static ClassRepository classRepository;
    
    @Autowired
    public void setClassRepository(ClassRepository classRepository) {
        ClassMapper.classRepository = classRepository;
    }
    
    public static ClassResponseDTO toDTO(ClassEntity classEntity) {
        if (classEntity == null) return null;
        
        ClassResponseDTO dto = new ClassResponseDTO();
        dto.setId(classEntity.getId());
        dto.setClassName(classEntity.getClassName());
        dto.setCreatedAt(classEntity.getCreatedAt());
        
        // Map divisions
        try {
            if (classEntity.getDivisions() != null) {
                List<DivisionResponseDTO> divisions = classEntity.getDivisions().stream()
                    .map(DivisionMapper::toDTO)
                    .collect(Collectors.toList());
                dto.setDivisions(divisions);
            } else {
                dto.setDivisions(new ArrayList<>());
            }
        } catch (Exception e) {
            dto.setDivisions(new ArrayList<>());
        }
        
        // Get student count via repository query
        try {
            if (classRepository != null && classEntity.getId() != null) {
                Integer count = classRepository.countStudentsByClassId(classEntity.getId());
                dto.setStudentCount(count != null ? count : 0);
            } else {
                dto.setStudentCount(0);
            }
        } catch (Exception e) {
            dto.setStudentCount(0);
        }
        
        // Get teacher count via repository query
        try {
            if (classRepository != null && classEntity.getId() != null) {
                Integer count = classRepository.countTeachersByClassId(classEntity.getId());
                dto.setTeacherCount(count != null ? count : 0);
            } else {
                dto.setTeacherCount(0);
            }
        } catch (Exception e) {
            dto.setTeacherCount(0);
        }
        
        return dto;
    }
    
    public static List<ClassResponseDTO> toDTOList(List<ClassEntity> classes) {
        if (classes == null) return new ArrayList<>();
        
        return classes.stream()
            .map(ClassMapper::toDTO)
            .collect(Collectors.toList());
    }
}
