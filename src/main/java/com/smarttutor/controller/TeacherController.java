package com.smarttutor.controller;

import com.smarttutor.entity.Teacher;
import com.smarttutor.entity.Student;
import com.smarttutor.entity.ClassEntity;
import com.smarttutor.entity.Division;
import com.smarttutor.entity.ActivityLog;
import com.smarttutor.enums.Role;
import com.smarttutor.entity.TeacherClassDivision;
import com.smarttutor.dto.StudentDTO;
import com.smarttutor.dto.response.TeacherResponseDTO;
import com.smarttutor.dto.response.StudentResponseDTO;
import com.smarttutor.dto.response.ClassResponseDTO;
import com.smarttutor.dto.response.DivisionResponseDTO;
import com.smarttutor.service.TeacherService;
import com.smarttutor.service.StudentService;
import com.smarttutor.service.ActivityLogService;
import com.smarttutor.service.EmailService;
import com.smarttutor.service.TeacherAssignmentService;
import com.smarttutor.repository.TeacherClassDivisionRepository;
import com.smarttutor.mapper.TeacherMapper;
import com.smarttutor.mapper.StudentMapper;
import com.smarttutor.mapper.ClassMapper;
import com.smarttutor.mapper.DivisionMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/teachers")
@CrossOrigin(origins = "*", maxAge = 3600)
public class TeacherController {
    
    @Autowired
    private TeacherService teacherService;
    
    @Autowired
    private StudentService studentService;
    
    @Autowired
    private ActivityLogService activityLogService;
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private TeacherAssignmentService assignmentService;
    
    @Autowired
    private TeacherClassDivisionRepository teacherClassDivisionRepository;
    
    @PostMapping
    public ResponseEntity<TeacherResponseDTO> createTeacher(@Valid @RequestBody Teacher teacher) {
        try {
            Teacher createdTeacher = teacherService.createTeacher(teacher);
            TeacherResponseDTO dto = TeacherMapper.toDTO(createdTeacher);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // TEACHER CRUD (DTO IMPLEMENTED)
    @GetMapping
    public ResponseEntity<List<TeacherResponseDTO>> getAllTeachers() {
        List<Teacher> teachers = teacherService.getAllTeachers();
        List<TeacherResponseDTO> dtos = TeacherMapper.toDTOList(teachers);
        return ResponseEntity.ok(dtos);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TeacherResponseDTO> getTeacherById(@PathVariable Long id) {
        try {
            Teacher teacher = teacherService.getTeacherById(id);
            TeacherResponseDTO dto = TeacherMapper.toDTO(teacher);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<TeacherResponseDTO> updateTeacher(@PathVariable Long id, @Valid @RequestBody Teacher teacher) {
        try {
            Teacher updatedTeacher = teacherService.updateTeacher(id, teacher);
            
            // Log activity
            activityLogService.logActivity(
                Role.TEACHER,
                id,
                "UPDATE_PROFILE",
                "Updated profile information"
            );
            
            TeacherResponseDTO dto = TeacherMapper.toDTO(updatedTeacher);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTeacher(@PathVariable Long id) {
        try {
            teacherService.deleteTeacher(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // STUDENT MANAGEMENT FOR TEACHERS (UPDATED FOR NEW MODEL)
    @PostMapping("/{teacherId}/students")
    public ResponseEntity<?> createStudentByTeacher(@PathVariable Long teacherId, @Valid @RequestBody StudentDTO studentDTO) {
        try {
            // Validate phone number for initial password
            if (studentDTO.getPhone() == null || studentDTO.getPhone().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Phone number is required as initial password");
            }
            
            // For now, require classId and divisionId to be provided in the request
            // In a real implementation, you might want to handle this differently
            if (studentDTO.getClassId() == null || studentDTO.getDivisionId() == null) {
                return ResponseEntity.badRequest().body("Class and division are required");
            }
            
            // Set password as phone number
            studentDTO.setPassword(studentDTO.getPhone());
            
            // Create student with explicit teacher assignment
            Student createdStudent = studentService.createStudentForTeacher(studentDTO, teacherId);
            
            // Send account creation email
            emailService.sendAccountCreationEmail(
                createdStudent.getName(),
                createdStudent.getEmail(),
                createdStudent.getPhone()
            );
            
            // Log activity
            activityLogService.logActivity(
                Role.TEACHER,
                teacherId,
                "CREATE_STUDENT",
                "Created new student: " + createdStudent.getName() + " (" + createdStudent.getEmail() + ")"
            );
            
            return ResponseEntity.ok(StudentMapper.toDTO(createdStudent));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
    @GetMapping("/{teacherId}/students")
    public ResponseEntity<List<StudentResponseDTO>> getStudentsByTeacher(@PathVariable Long teacherId) {
        try {
            // Get all class-division assignments for this teacher
            List<TeacherClassDivision> assignments = teacherClassDivisionRepository.findByTeacherId(teacherId);
            
            List<Student> allStudents = new ArrayList<>();
            
            // Get students from all assigned classes and divisions
            for (TeacherClassDivision assignment : assignments) {
                List<Student> students = studentService.getStudentsByClassAndDivision(
                    assignment.getClassEntity().getId(), 
                    assignment.getDivision().getId()
                );
                allStudents.addAll(students);
            }
            
            // Remove duplicates and create DTOs
            List<Student> uniqueStudents = allStudents.stream()
                .distinct()
                .collect(Collectors.toList());
            
            List<StudentResponseDTO> dtos = new ArrayList<>();
            for (Student student : uniqueStudents) {
                // Get all teachers for this student's class/division using the new method
                List<Teacher> assignedTeachers = teacherClassDivisionRepository
                    .findTeachersByClassIdAndDivisionIdAndTeacherActive(
                        student.getClassEntity().getId(), 
                        student.getDivision().getId(), 
                        true
                    );
                
                StudentResponseDTO dto = StudentMapper.toDTOWithAllTeachers(student, assignedTeachers);
                dtos.add(dto);
            }
            
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // TEACHER DASHBOARD DATA
    @GetMapping("/{teacherId}/dashboard")
    public ResponseEntity<?> getTeacherDashboard(@PathVariable Long teacherId) {
        try {
            List<Student> students = studentService.getStudentsByTeacher(teacherId);
            
            // Get recent activity for this teacher
            List<ActivityLog> recentActivity = activityLogService.getRecentActivitiesByUser(Role.TEACHER, teacherId, 5);
            
            return ResponseEntity.ok(Map.of(
                "totalStudents", students.size(),
                "activeStudents", students.stream().mapToInt(s -> s.getActive() ? 1 : 0).sum(),
                "recentActivity", recentActivity.stream().map(activity -> Map.of(
                    "action", activity.getAction(),
                    "details", activity.getDescription(),
                    "timestamp", activity.getCreatedAt()
                )).toList()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    // NEW MULTI-CLASS/DIVISION APIS
    
    @GetMapping("/{teacherId}/classes")
    public ResponseEntity<List<ClassResponseDTO>> getTeacherClasses(@PathVariable Long teacherId) {
        try {
            List<ClassEntity> classes = assignmentService.getTeacherClasses(teacherId);
            List<ClassResponseDTO> dtos = ClassMapper.toDTOList(classes);
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/{teacherId}/divisions")
    public ResponseEntity<List<DivisionResponseDTO>> getTeacherDivisions(
            @PathVariable Long teacherId, 
            @RequestParam(required = false) Long classId) {
        try {
            List<Division> divisions;
            
            if (classId != null) {
                // Get divisions for specific class
                divisions = assignmentService.getTeacherDivisionsByClass(teacherId, classId);
            } else {
                // Get all divisions for teacher (across all classes)
                List<ClassEntity> teacherClasses = assignmentService.getTeacherClasses(teacherId);
                divisions = teacherClasses.stream()
                    .flatMap(classEntity -> classEntity.getDivisions().stream())
                    .distinct()
                    .toList();
            }
            
            List<DivisionResponseDTO> dtos = DivisionMapper.toDTOList(divisions);
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/{teacherId}/students/filter")
    public ResponseEntity<List<StudentResponseDTO>> getTeacherStudentsByClassAndDivision(
            @PathVariable Long teacherId,
            @RequestParam(required = false) Long classId,
            @RequestParam(required = false) Long divisionId) {
        try {
            List<Student> students;
            
            if (classId != null && divisionId != null) {
                // Get students for specific class and division
                students = studentService.getStudentsByClassAndDivision(classId, divisionId)
                    .stream()
                    .filter(student -> student.getTeacher() != null && student.getTeacher().getId().equals(teacherId))
                    .toList();
            } else if (classId != null) {
                // Get students for specific class (all divisions)
                students = studentService.getStudentsByClass(classId)
                    .stream()
                    .filter(student -> student.getTeacher() != null && student.getTeacher().getId().equals(teacherId))
                    .toList();
            } else {
                // Get all students for teacher
                students = studentService.getStudentsByTeacher(teacherId);
            }
            
            List<StudentResponseDTO> dtos = StudentMapper.toDTOList(students);
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @PostMapping("/{teacherId}/assignments")
    public ResponseEntity<?> assignTeacherToClassDivision(
            @PathVariable Long teacherId,
            @RequestBody List<TeacherAssignmentService.ClassDivisionAssignment> assignments) {
        try {
            List<com.smarttutor.entity.TeacherClassDivision> results = 
                assignmentService.assignTeacherToMultipleClasses(teacherId, assignments);
            
            // Log activity
            activityLogService.logActivity(
                Role.TEACHER,
                teacherId,
                "ASSIGN_CLASSES",
                "Assigned to " + results.size() + " class-division combinations"
            );
            
            return ResponseEntity.ok(Map.of("assignments", results.size(), "message", "Assignments completed"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
    @DeleteMapping("/{teacherId}/assignments/{classId}/{divisionId}")
    public ResponseEntity<?> removeTeacherAssignment(
            @PathVariable Long teacherId,
            @PathVariable Long classId,
            @PathVariable Long divisionId) {
        try {
            assignmentService.removeTeacherAssignment(teacherId, classId, divisionId);
            
            // Log activity
            activityLogService.logActivity(
                Role.TEACHER,
                teacherId,
                "REMOVE_ASSIGNMENT",
                "Removed assignment from class " + classId + " division " + divisionId
            );
            
            return ResponseEntity.ok(Map.of("message", "Assignment removed successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
