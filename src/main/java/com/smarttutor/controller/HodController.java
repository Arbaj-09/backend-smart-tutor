package com.smarttutor.controller;

import com.smarttutor.dto.DashboardStatsDTO;
import com.smarttutor.entity.*;
import com.smarttutor.enums.Role;
import com.smarttutor.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/hod")
@CrossOrigin(origins = "*", maxAge = 3600)
public class HodController {
    
    @Autowired
    private HodService hodService;
    
    @Autowired
    private ClassService classService;
    
    @Autowired
    private DivisionService divisionService;
    
    @Autowired
    private TeacherService teacherService;
    
    @Autowired
    private StudentService studentService;
    
    @Autowired
    private ActivityLogService activityLogService;
    
    @PostMapping("/register")
    public ResponseEntity<?> registerHod(@RequestBody Hod hod) {
        try {
            Hod registered = hodService.createHod(hod);
            return ResponseEntity.ok(registered);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
    @GetMapping("/dashboard")
    public ResponseEntity<DashboardStatsDTO> getHODDashboard() {
        DashboardStatsDTO stats = hodService.getHODDashboardStats();
        return ResponseEntity.ok(stats);
    }
    
    // CLASS MANAGEMENT
    @PostMapping("/class")
    public ResponseEntity<ClassEntity> createClass(@Valid @RequestBody ClassEntity classEntity) {
        try {
            ClassEntity createdClass = classService.createClass(classEntity);
            
            // Log activity (without security context)
            activityLogService.logActivity(
                Role.HOD,
                1L, // Default user ID for now
                "CREATE_CLASS",
                "Created new class: " + classEntity.getClassName()
            );
            
            return ResponseEntity.ok(createdClass);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/classes")
    public ResponseEntity<List<ClassEntity>> getAllClasses() {
        List<ClassEntity> classes = classService.getAllClasses();
        return ResponseEntity.ok(classes);
    }
    
    // DIVISION MANAGEMENT
    @PostMapping("/division")
    public ResponseEntity<Division> createDivision(@Valid @RequestBody Division division) {
        try {
            Division createdDivision = divisionService.createDivision(division);
            
            // Log activity (without security context)
            activityLogService.logActivity(
                Role.HOD,
                1L, // Default user ID for now
                "CREATE_DIVISION",
                "Created new division: " + division.getDivisionName()
            );
            
            return ResponseEntity.ok(createdDivision);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/divisions")
    public ResponseEntity<List<Division>> getAllDivisions() {
        List<Division> divisions = divisionService.getAllDivisions();
        return ResponseEntity.ok(divisions);
    }
    
    @GetMapping("/divisions/class/{classId}")
    public ResponseEntity<List<Division>> getDivisionsByClass(@PathVariable Long classId) {
        List<Division> divisions = divisionService.getDivisionsByClass(classId);
        return ResponseEntity.ok(divisions);
    }
    
    // TEACHER MANAGEMENT
    @PostMapping("/teacher")
    public ResponseEntity<Teacher> createTeacher(@Valid @RequestBody Teacher teacher) {
        try {
            Teacher createdTeacher = teacherService.createTeacher(teacher);
            
            // Log activity (without security context)
            activityLogService.logActivity(
                Role.HOD,
                1L, // Default user ID for now
                "CREATE_TEACHER",
                "Created new teacher: " + teacher.getName() + " (" + teacher.getEmail() + ")"
            );
            
            return ResponseEntity.ok(createdTeacher);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/teachers")
    public ResponseEntity<List<Teacher>> getAllTeachers() {
        List<Teacher> teachers = teacherService.getAllTeachers();
        return ResponseEntity.ok(teachers);
    }
    
    @PutMapping("/teachers/{id}")
    public ResponseEntity<Teacher> updateTeacher(@PathVariable Long id, @Valid @RequestBody Teacher teacher) {
        try {
            Teacher updatedTeacher = teacherService.updateTeacher(id, teacher);
            
            // Log activity (without security context)
            activityLogService.logActivity(
                Role.HOD,
                1L, // Default user ID for now
                "UPDATE_TEACHER",
                "Updated teacher: " + teacher.getName()
            );
            
            return ResponseEntity.ok(updatedTeacher);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/teachers/{id}")
    public ResponseEntity<?> deleteTeacher(@PathVariable Long id) {
        try {
            teacherService.deleteTeacher(id);
            
            // Log activity (without security context)
            activityLogService.logActivity(
                Role.HOD,
                1L, // Default user ID for now
                "DELETE_TEACHER",
                "Deleted teacher with ID: " + id
            );
            
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // STUDENT VIEWING
    @GetMapping("/students")
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }
    
    @GetMapping("/students/class/{classId}/division/{divisionId}")
    public ResponseEntity<List<Student>> getStudentsByClassAndDivision(
            @PathVariable Long classId, @PathVariable Long divisionId) {
        List<Student> students = studentService.getStudentsByClassAndDivision(classId, divisionId);
        return ResponseEntity.ok(students);
    }
    
    // ACTIVITY LOGS
    @GetMapping("/activity-logs")
    public ResponseEntity<List<ActivityLog>> getActivityLogs() {
        List<ActivityLog> logs = activityLogService.getAllActivityLogs();
        return ResponseEntity.ok(logs);
    }
}
