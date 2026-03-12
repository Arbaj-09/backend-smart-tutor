package com.smarttutor.controller;

import com.smarttutor.entity.ClassEntity;
import com.smarttutor.dto.response.ClassResponseDTO;
import com.smarttutor.service.ClassService;
import com.smarttutor.mapper.ClassMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/classes")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ClassController {
    
    @Autowired
    private ClassService classService;
    
    @PostMapping
    public ResponseEntity<ClassResponseDTO> createClass(@Valid @RequestBody ClassEntity classEntity) {
        try {
            ClassEntity createdClass = classService.createClass(classEntity);
            ClassResponseDTO dto = ClassMapper.toDTO(createdClass);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping
    public ResponseEntity<List<ClassResponseDTO>> getAllClasses() {
        List<ClassEntity> classes = classService.getAllClasses();
        List<ClassResponseDTO> dtos = ClassMapper.toDTOList(classes);
        return ResponseEntity.ok(dtos);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ClassResponseDTO> getClassById(@PathVariable Long id) {
        try {
            ClassEntity classEntity = classService.getClassById(id);
            ClassResponseDTO dto = ClassMapper.toDTO(classEntity);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ClassResponseDTO> updateClass(@PathVariable Long id, @Valid @RequestBody ClassEntity classEntity) {
        try {
            ClassEntity updatedClass = classService.updateClass(id, classEntity);
            ClassResponseDTO dto = ClassMapper.toDTO(updatedClass);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClass(@PathVariable Long id) {
        try {
            classService.deleteClass(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
