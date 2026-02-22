package com.smarttutor.service;

import com.smarttutor.entity.ClassEntity;
import com.smarttutor.exception.ResourceNotFoundException;
import com.smarttutor.repository.ClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassService {
    
    @Autowired
    private ClassRepository classRepository;
    
    public ClassEntity createClass(ClassEntity classEntity) {
        if (classRepository.existsByClassName(classEntity.getClassName())) {
            throw new IllegalArgumentException("Class with name " + classEntity.getClassName() + " already exists");
        }
        return classRepository.save(classEntity);
    }
    
    public ClassEntity getClassById(Long id) {
        return classRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Class", "id", id));
    }
    
    public List<ClassEntity> getAllClasses() {
        return classRepository.findAll();
    }
    
    public ClassEntity updateClass(Long id, ClassEntity classEntity) {
        ClassEntity existingClass = getClassById(id);
        
        existingClass.setClassName(classEntity.getClassName());
        
        return classRepository.save(existingClass);
    }
    
    public void deleteClass(Long id) {
        ClassEntity classEntity = getClassById(id);
        classRepository.delete(classEntity);
    }
}
