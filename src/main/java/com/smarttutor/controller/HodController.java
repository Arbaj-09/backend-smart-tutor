package com.smarttutor.controller;

import com.smarttutor.entity.Teacher;
import com.smarttutor.entity.Student;
import com.smarttutor.entity.ClassEntity;
import com.smarttutor.entity.Division;
import com.smarttutor.entity.ActivityLog;
import com.smarttutor.entity.Hod;
import com.smarttutor.enums.Role;
import com.smarttutor.dto.StudentDTO;
import com.smarttutor.service.TeacherService;
import com.smarttutor.service.StudentService;
import com.smarttutor.service.ClassService;
import com.smarttutor.service.DivisionService;
import com.smarttutor.service.ActivityLogService;
import com.smarttutor.service.HodService;
import com.smarttutor.service.EmailService;
import com.smarttutor.dto.DashboardStatsDTO;
import com.smarttutor.dto.response.ClassResponseDTO;
import com.smarttutor.dto.response.DivisionResponseDTO;
import com.smarttutor.dto.response.TeacherResponseDTO;
import com.smarttutor.dto.response.StudentResponseDTO;
import com.smarttutor.mapper.ClassMapper;
import com.smarttutor.entity.Teacher;
import com.smarttutor.entity.TeacherClassDivision;
import com.smarttutor.mapper.DivisionMapper;
import com.smarttutor.mapper.TeacherMapper;
import com.smarttutor.mapper.StudentMapper;
import com.smarttutor.repository.TeacherClassDivisionRepository;
import com.smarttutor.repository.TeacherRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/hod")
@CrossOrigin(origins = "*", maxAge = 3600)
public class HodController {
    
    @Autowired
    private TeacherService teacherService;
    
    @Autowired
    private StudentService studentService;
    
    @Autowired
    private TeacherClassDivisionRepository teacherClassDivisionRepository;
    
    @Autowired
    private TeacherRepository teacherRepository;
    
    @Autowired
    private ClassService classService;
    
    @Autowired
    private DivisionService divisionService;
    
    @Autowired
    private ActivityLogService activityLogService;
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private HodService hodService;
    
    @PostMapping("/register")
    public ResponseEntity<?> registerHod(@RequestBody Hod hod) {
        try {
            Hod registered = hodService.createHod(hod);
            return ResponseEntity.ok(registered);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
    // CLASS MANAGEMENT
    @PostMapping("/class")
    public ResponseEntity<ClassResponseDTO> createClass(@Valid @RequestBody ClassEntity classEntity) {
        try {
            ClassEntity createdClass = classService.createClass(classEntity);
            ClassResponseDTO dto = ClassMapper.toDTO(createdClass);
            
            // Log activity (without security context)
            activityLogService.logActivity(
                Role.HOD,
                1L, // Default user ID for now
                "CREATE_CLASS",
                "Created new class: " + classEntity.getClassName()
            );
            
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/classes")
    public ResponseEntity<List<ClassResponseDTO>> getAllClasses() {
        List<ClassEntity> classes = classService.getAllClasses();
        List<ClassResponseDTO> dtos = ClassMapper.toDTOList(classes);
        return ResponseEntity.ok(dtos);
    }
    
    @PutMapping("/classes/{id}")
    public ResponseEntity<ClassResponseDTO> updateClass(@PathVariable Long id, @Valid @RequestBody ClassEntity classEntity) {
        try {
            ClassEntity updatedClass = classService.updateClass(id, classEntity);
            ClassResponseDTO dto = ClassMapper.toDTO(updatedClass);
            
            activityLogService.logActivity(
                Role.HOD,
                1L,
                "UPDATE_CLASS",
                "Updated class: " + classEntity.getClassName()
            );
            
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/classes/{id}")
    public ResponseEntity<?> deleteClass(@PathVariable Long id) {
        try {
            classService.deleteClass(id);
            
            activityLogService.logActivity(
                Role.HOD,
                1L,
                "DELETE_CLASS",
                "Deleted class with ID: " + id
            );
            
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // DIVISION MANAGEMENT
    @PostMapping("/division")
    public ResponseEntity<DivisionResponseDTO> createDivision(@Valid @RequestBody Division division) {
        try {
            Division createdDivision = divisionService.createDivision(division);
            DivisionResponseDTO dto = DivisionMapper.toDTO(createdDivision);
            
            // Log activity (without security context)
            activityLogService.logActivity(
                Role.HOD,
                1L, // Default user ID for now
                "CREATE_DIVISION",
                "Created new division: " + division.getDivisionName()
            );
            
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/divisions")
    public ResponseEntity<List<DivisionResponseDTO>> getAllDivisions() {
        List<Division> divisions = divisionService.getAllDivisions();
        List<DivisionResponseDTO> dtos = DivisionMapper.toDTOList(divisions);
        return ResponseEntity.ok(dtos);
    }
    
    @GetMapping("/divisions/class/{classId}")
    public ResponseEntity<List<DivisionResponseDTO>> getDivisionsByClass(@PathVariable Long classId) {
        List<Division> divisions = divisionService.getDivisionsByClass(classId);
        List<DivisionResponseDTO> dtos = DivisionMapper.toDTOList(divisions);
        return ResponseEntity.ok(dtos);
    }
    
    @PutMapping("/divisions/{id}")
    public ResponseEntity<DivisionResponseDTO> updateDivision(@PathVariable Long id, @Valid @RequestBody Division division) {
        try {
            Division updatedDivision = divisionService.updateDivision(id, division);
            DivisionResponseDTO dto = DivisionMapper.toDTO(updatedDivision);
            
            activityLogService.logActivity(
                Role.HOD,
                1L,
                "UPDATE_DIVISION",
                "Updated division: " + division.getDivisionName()
            );
            
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/divisions/{id}")
    public ResponseEntity<?> deleteDivision(@PathVariable Long id) {
        try {
            divisionService.deleteDivision(id);
            
            activityLogService.logActivity(
                Role.HOD,
                1L,
                "DELETE_DIVISION",
                "Deleted division with ID: " + id
            );
            
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // TEACHER MANAGEMENT
    @PostMapping("/teacher")
    public ResponseEntity<?> createTeacher(@Valid @RequestBody Teacher teacher) {
        try {
            // Use mobile number as initial password and encode it
            if (teacher.getPhone() == null || teacher.getPhone().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Phone number is required as initial password");
            }
            
            // Set password as phone number and encode it
            teacher.setPassword(teacher.getPhone());
            
            Teacher createdTeacher = teacherService.createTeacher(teacher);
            
            // Send account creation email
            emailService.sendAccountCreationEmail(
                createdTeacher.getName(),
                createdTeacher.getEmail(),
                createdTeacher.getPhone()
            );
            
            // Log activity (without security context)
            activityLogService.logActivity(
                Role.HOD,
                1L, // Default user ID for now
                "CREATE_TEACHER",
                "Created new teacher: " + createdTeacher.getName() + " (" + createdTeacher.getEmail() + ")"
            );
            
            return ResponseEntity.ok(TeacherMapper.toDTO(createdTeacher));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
    @GetMapping("/teachers")
    public ResponseEntity<List<TeacherResponseDTO>> getAllTeachers() {
        List<Teacher> teachers = teacherService.getAllTeachers();
        List<TeacherResponseDTO> dtos = TeacherMapper.toDTOList(teachers);
        return ResponseEntity.ok(dtos);
    }
    
    @PutMapping("/teachers/{id}")
    public ResponseEntity<TeacherResponseDTO> updateTeacher(@PathVariable Long id, @Valid @RequestBody Map<String, Object> teacherData) {
        try {
            Teacher teacher = teacherService.getTeacherById(id);
            
            // Update basic fields
            if (teacherData.containsKey("name")) teacher.setName((String) teacherData.get("name"));
            if (teacherData.containsKey("email")) teacher.setEmail((String) teacherData.get("email"));
            if (teacherData.containsKey("phone")) teacher.setPhone((String) teacherData.get("phone"));
            if (teacherData.containsKey("subject")) teacher.setSubject((String) teacherData.get("subject"));
            if (teacherData.containsKey("active")) teacher.setActive((Boolean) teacherData.get("active"));
            if (teacherData.containsKey("fcmToken")) teacher.setFcmToken((String) teacherData.get("fcmToken"));
            
            Teacher updatedTeacher = teacherService.updateTeacher(id, teacher);
            
            // Handle class/division assignment
            if (teacherData.containsKey("classId") && teacherData.containsKey("divisionId")) {
                Object classIdObj = teacherData.get("classId");
                Object divisionIdObj = teacherData.get("divisionId");
                
                if (classIdObj != null && divisionIdObj != null && !classIdObj.toString().isEmpty() && !divisionIdObj.toString().isEmpty()) {
                    Long classId = Long.valueOf(classIdObj.toString());
                    Long divisionId = Long.valueOf(divisionIdObj.toString());
                    
                    ClassEntity classEntity = classService.getClassById(classId);
                    Division division = divisionService.getDivisionById(divisionId);
                    
                    // Remove old assignments for this teacher
                    teacherService.clearTeacherAssignments(id);
                    
                    // Create new assignment
                    teacherService.assignTeacherToClassDivision(updatedTeacher, classEntity, division);
                }
            }
            
            // Log activity
            activityLogService.logActivity(
                Role.HOD,
                1L,
                "UPDATE_TEACHER",
                "Updated teacher: " + teacher.getName()
            );
            
            // Reload teacher with assignments
            updatedTeacher = teacherService.getTeacherById(id);
            return ResponseEntity.ok(TeacherMapper.toDTO(updatedTeacher));
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
    
    // STUDENT MANAGEMENT
    @PostMapping("/student")
    public ResponseEntity<?> createStudent(@Valid @RequestBody StudentDTO studentDTO) {
        try {
            // Use mobile number as initial password and encode it
            if (studentDTO.getPhone() == null || studentDTO.getPhone().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Phone number is required as initial password");
            }
            
            // Set password as phone number
            studentDTO.setPassword(studentDTO.getPhone());
            
            Student createdStudent = studentService.createStudent(studentDTO);
            
            // Send account creation email
            emailService.sendAccountCreationEmail(
                createdStudent.getName(),
                createdStudent.getEmail(),
                createdStudent.getPhone()
            );
            
            // Log activity
            activityLogService.logActivity(
                Role.HOD,
                1L,
                "CREATE_STUDENT",
                "Created new student: " + createdStudent.getName() + " (" + createdStudent.getEmail() + ")"
            );
            
            // Return simple success response to avoid circular reference issues
            return ResponseEntity.ok(Map.of(
                "message", "Student created successfully",
                "id", createdStudent.getId(),
                "name", createdStudent.getName(),
                "email", createdStudent.getEmail()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
    @GetMapping("/students")
    public ResponseEntity<List<StudentResponseDTO>> getAllStudents() {
        try {
            List<Student> students = studentService.getAllStudents();
            List<StudentResponseDTO> dtos = new ArrayList<>();
            
            for (Student student : students) {
                // Get all teachers assigned to this student's class and division using the new method
                List<Teacher> assignedTeachers = teacherClassDivisionRepository
                    .findTeachersByClassIdAndDivisionIdAndTeacherActive(
                        student.getClassEntity().getId(), 
                        student.getDivision().getId(), 
                        true
                    );
                
                // Create DTO with all teachers
                StudentResponseDTO dto = StudentMapper.toDTOWithAllTeachers(student, assignedTeachers);
                dtos.add(dto);
            }
            
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            // If there's a circular reference issue, return empty list with error message
            System.err.println("Error getting students: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.ok(new ArrayList<>());
        }
    }
    
    @GetMapping("/students/class/{classId}/division/{divisionId}")
    public ResponseEntity<List<StudentResponseDTO>> getStudentsByClassAndDivision(
            @PathVariable Long classId, @PathVariable Long divisionId) {
        List<Student> students = studentService.getStudentsByClassAndDivision(classId, divisionId);
        List<StudentResponseDTO> dtos = StudentMapper.toDTOList(students);
        return ResponseEntity.ok(dtos);
    }
    
    @PutMapping("/students/{id}")
    public ResponseEntity<StudentResponseDTO> updateStudent(@PathVariable Long id, @Valid @RequestBody StudentDTO studentDTO) {
        try {
            Student updatedStudent = studentService.updateStudent(id, studentDTO);
            StudentResponseDTO dto = StudentMapper.toDTO(updatedStudent);
            
            activityLogService.logActivity(
                Role.HOD,
                1L,
                "UPDATE_STUDENT",
                "Updated student: " + studentDTO.getName()
            );
            
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/students/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable Long id) {
        try {
            studentService.deleteStudent(id);
            
            activityLogService.logActivity(
                Role.HOD,
                1L,
                "DELETE_STUDENT",
                "Deleted student with ID: " + id
            );
            
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // ACTIVITY LOGS
    @GetMapping("/activity-logs")
    public ResponseEntity<List<ActivityLog>> getActivityLogs() {
        List<ActivityLog> logs = activityLogService.getAllActivityLogs();
        return ResponseEntity.ok(logs);
    }
    
    // EMAIL FUNCTIONALITY
    @PostMapping("/teachers/{id}/send-login-email")
    public ResponseEntity<?> sendLoginEmail(@PathVariable Long id) {
        try {
            Teacher teacher = teacherService.getTeacherById(id);
            
            if (teacher == null) {
                return ResponseEntity.notFound().build();
            }
            
            // Send email with login credentials
            emailService.sendLoginCredentials(
                teacher.getName(),
                teacher.getEmail(),
                teacher.getEmail(), // Login email is the same as teacher email
                teacher.getPassword() // Send the stored password
            );
            
            // Log activity
            activityLogService.logActivity(
                Role.HOD,
                1L, // Default user ID for now
                "SEND_LOGIN_EMAIL",
                "Sent login email to teacher: " + teacher.getName() + " (" + teacher.getEmail() + ")"
            );
            
            return ResponseEntity.ok(Map.of(
                "message", "Login credentials sent successfully",
                "teacherName", teacher.getName(),
                "teacherEmail", teacher.getEmail()
            ));
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "error", "Failed to send login email: " + e.getMessage()
            ));
        }
    }
}
