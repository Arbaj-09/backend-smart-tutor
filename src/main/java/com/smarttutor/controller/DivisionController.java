package com.smarttutor.controller;

import com.smarttutor.entity.Division;
import com.smarttutor.service.DivisionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/divisions")
@CrossOrigin(origins = "*", maxAge = 3600)
public class DivisionController {
    
    @Autowired
    private DivisionService divisionService;
    
    @PostMapping
    public ResponseEntity<Division> createDivision(@Valid @RequestBody Division division) {
        try {
            Division createdDivision = divisionService.createDivision(division);
            return ResponseEntity.ok(createdDivision);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping
    public ResponseEntity<List<Division>> getAllDivisions() {
        List<Division> divisions = divisionService.getAllDivisions();
        return ResponseEntity.ok(divisions);
    }
    
    @GetMapping("/class/{classId}")
    public ResponseEntity<List<Division>> getDivisionsByClass(@PathVariable Long classId) {
        try {
            List<Division> divisions = divisionService.getDivisionsByClass(classId);
            return ResponseEntity.ok(divisions);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Division> getDivisionById(@PathVariable Long id) {
        try {
            Division division = divisionService.getDivisionById(id);
            return ResponseEntity.ok(division);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Division> updateDivision(@PathVariable Long id, @Valid @RequestBody Division division) {
        try {
            Division updatedDivision = divisionService.updateDivision(id, division);
            return ResponseEntity.ok(updatedDivision);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDivision(@PathVariable Long id) {
        try {
            divisionService.deleteDivision(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
