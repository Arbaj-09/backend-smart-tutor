package com.smarttutor.service;

import com.smarttutor.entity.Division;
import com.smarttutor.entity.ClassEntity;
import com.smarttutor.exception.ResourceNotFoundException;
import com.smarttutor.repository.DivisionRepository;
import com.smarttutor.repository.ClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DivisionService {
    
    @Autowired
    private DivisionRepository divisionRepository;
    
    @Autowired
    private ClassRepository classRepository;
    
    public Division createDivision(Division division) {
        ClassEntity classEntity = classRepository.findById(division.getClassEntity().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Class", "id", division.getClassEntity().getId()));
        
        if (divisionRepository.existsByDivisionNameAndClassEntityId(
                division.getDivisionName(), classEntity.getId())) {
            throw new IllegalArgumentException("Division " + division.getDivisionName() + 
                    " already exists for class " + classEntity.getClassName());
        }
        
        division.setClassEntity(classEntity);
        return divisionRepository.save(division);
    }
    
    public Division getDivisionById(Long id) {
        return divisionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Division", "id", id));
    }
    
    public List<Division> getAllDivisions() {
        return divisionRepository.findAll();
    }
    
    public List<Division> getDivisionsByClass(Long classId) {
        ClassEntity classEntity = classRepository.findById(classId)
                .orElseThrow(() -> new ResourceNotFoundException("Class", "id", classId));
        return divisionRepository.findByClassId(classId);
    }
    
    public Division updateDivision(Long id, Division division) {
        Division existingDivision = getDivisionById(id);
        
        existingDivision.setDivisionName(division.getDivisionName());
        
        if (division.getClassEntity() != null) {
            ClassEntity classEntity = classRepository.findById(division.getClassEntity().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Class", "id", division.getClassEntity().getId()));
            existingDivision.setClassEntity(classEntity);
        }
        
        return divisionRepository.save(existingDivision);
    }
    
    public void deleteDivision(Long id) {
        Division division = getDivisionById(id);
        divisionRepository.delete(division);
    }
}
