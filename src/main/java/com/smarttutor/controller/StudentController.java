package com.smarttutor.controller;

import com.smarttutor.dto.StudentDTO;
import com.smarttutor.dto.response.StudentResponseDTO;
import com.smarttutor.entity.Student;
import com.smarttutor.service.StudentService;
import com.smarttutor.mapper.StudentMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*", maxAge = 3600)
public class StudentController {
    
    @Autowired
    private StudentService studentService;
    
    @PostMapping
    public ResponseEntity<StudentResponseDTO> createStudent(@Valid @RequestBody StudentDTO studentDTO) {
        try {
            Student createdStudent = studentService.createStudent(studentDTO);
            StudentResponseDTO dto = StudentMapper.toDTO(createdStudent);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping
    public ResponseEntity<List<StudentResponseDTO>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        List<StudentResponseDTO> dtos = StudentMapper.toDTOList(students);
        return ResponseEntity.ok(dtos);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> getStudentById(@PathVariable Long id) {
        try {
            Student student = studentService.getStudentById(id);
            StudentResponseDTO dto = StudentMapper.toDTO(student);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> updateStudent(@PathVariable Long id, @Valid @RequestBody StudentDTO studentDTO) {
        try {
            Student updatedStudent = studentService.updateStudent(id, studentDTO);
            StudentResponseDTO dto = StudentMapper.toDTO(updatedStudent);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable Long id) {
        try {
            studentService.deleteStudent(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/class/{classId}/division/{divisionId}")
    public ResponseEntity<List<Student>> getStudentsByClassAndDivision(
            @PathVariable Long classId, @PathVariable Long divisionId) {
        List<Student> students = studentService.getStudentsByClassAndDivision(classId, divisionId);
        return ResponseEntity.ok(students);
    }
    
    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<Student>> getStudentsByTeacher(@PathVariable Long teacherId) {
        List<Student> students = studentService.getStudentsByTeacher(teacherId);
        return ResponseEntity.ok(students);
    }
}
