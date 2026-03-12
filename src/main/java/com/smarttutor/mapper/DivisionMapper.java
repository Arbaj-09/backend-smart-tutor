package com.smarttutor.mapper;

import com.smarttutor.dto.response.DivisionResponseDTO;
import com.smarttutor.entity.Division;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DivisionMapper {
    
    public static DivisionResponseDTO toDTO(Division division) {
        if (division == null) return null;
        
        DivisionResponseDTO dto = new DivisionResponseDTO();
        dto.setId(division.getId());
        dto.setDivisionName(division.getDivisionName());
        dto.setCreatedAt(division.getCreatedAt());
        
        // Map class - should work now with JOIN FETCH
        if (division.getClassEntity() != null) {
            dto.setClassId(division.getClassEntity().getId());
            dto.setClassName(division.getClassEntity().getClassName());
        }
        
        // Map student count
        if (division.getStudents() != null) {
            dto.setStudentCount(division.getStudents().size());
        } else {
            dto.setStudentCount(0);
        }
        
        return dto;
    }
    
    public static List<DivisionResponseDTO> toDTOList(List<Division> divisions) {
        if (divisions == null) return new ArrayList<>();
        
        return divisions.stream()
            .map(DivisionMapper::toDTO)
            .collect(Collectors.toList());
    }
}
