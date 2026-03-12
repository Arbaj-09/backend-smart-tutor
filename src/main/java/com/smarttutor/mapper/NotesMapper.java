package com.smarttutor.mapper;

import com.smarttutor.dto.response.NotesResponseDTO;
import com.smarttutor.dto.response.ClassResponseDTO;
import com.smarttutor.dto.response.DivisionResponseDTO;
import com.smarttutor.dto.response.TeacherResponseDTO;
import com.smarttutor.entity.Notes;

import java.util.List;
import java.util.stream.Collectors;

public class NotesMapper {
    
    public static NotesResponseDTO toDTO(Notes notes) {
        if (notes == null) {
            return null;
        }
        
        NotesResponseDTO dto = new NotesResponseDTO();
        dto.setId(notes.getId());
        dto.setTitle(notes.getTitle());
        dto.setSubject(notes.getSubject());
        dto.setDescription(notes.getDescription());
        dto.setFileUrl("http://localhost:8082/" + notes.getFilePath());
        dto.setCreatedAt(notes.getUploadedAt());
        
        // Set new flattened fields with enhanced null safety
        if(notes.getTeacher() != null && notes.getTeacher().getName() != null) {
            dto.setTeacherName(notes.getTeacher().getName());
        }

        if(notes.getClassEntity() != null && notes.getClassEntity().getClassName() != null) {
            dto.setClassName(notes.getClassEntity().getClassName());
        }

        if(notes.getDivision() != null && notes.getDivision().getDivisionName() != null) {
            dto.setDivisionName(notes.getDivision().getDivisionName());
        }
        
        // Keep existing fields for backward compatibility
        dto.setFileName(notes.getFileName());
        dto.setFileType(notes.getFileType());
        dto.setFileSize(notes.getFileSize());
        dto.setFilePath(notes.getFilePath());
        dto.setUploadedAt(notes.getUploadedAt());
        dto.setActive(notes.getActive());
        
        // Map related entities - handle lazy loading safely
        try {
            if (notes.getClassEntity() != null) {
                dto.setClassEntity(ClassMapper.toDTO(notes.getClassEntity()));
            }
        } catch (Exception e) {
            // Lazy loading failed, set null
            dto.setClassEntity(null);
        }
        
        try {
            if (notes.getDivision() != null) {
                dto.setDivision(DivisionMapper.toDTO(notes.getDivision()));
            }
        } catch (Exception e) {
            // Lazy loading failed, set null
            dto.setDivision(null);
        }
        
        try {
            if (notes.getTeacher() != null) {
                dto.setTeacher(TeacherMapper.toDTO(notes.getTeacher()));
            }
        } catch (Exception e) {
            // Lazy loading failed, set null
            dto.setTeacher(null);
        }
        
        return dto;
    }
    
    public static List<NotesResponseDTO> toDTOList(List<Notes> notesList) {
        if (notesList == null) {
            return null;
        }
        
        return notesList.stream()
                .map(NotesMapper::toDTO)
                .collect(Collectors.toList());
    }
}
