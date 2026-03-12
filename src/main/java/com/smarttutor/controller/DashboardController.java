package com.smarttutor.controller;

import com.smarttutor.repository.ClassRepository;
import com.smarttutor.repository.TeacherRepository;
import com.smarttutor.repository.StudentRepository;
import com.smarttutor.repository.QuizRepository;
import com.smarttutor.repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", maxAge = 3600)
public class DashboardController {
    
    @Autowired
    private ClassRepository classRepository;
    
    @Autowired
    private TeacherRepository teacherRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private QuizRepository quizRepository;
    
    @Autowired
    private AttendanceRepository attendanceRepository;
    
    @GetMapping("/student/dashboard")
    public ResponseEntity<Map<String, Object>> getStudentDashboard() {
        try {
            Map<String, Object> data = new HashMap<>();
            
            // Get counts
            data.put("classes", classRepository.count());
            data.put("teachers", teacherRepository.count());
            data.put("students", studentRepository.count());
            data.put("quizzes", quizRepository.count());
            
            // Get attendance statistics
            long presentCount = attendanceRepository.countByStatus("PRESENT");
            long totalAttendance = attendanceRepository.count();
            double attendancePercentage = totalAttendance > 0 ? 
                (presentCount * 100.0 / totalAttendance) : 0.0;
            data.put("attendancePercentage", Math.round(attendancePercentage * 100.0) / 100.0);
            
            return ResponseEntity.ok(data);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/teacher/dashboard")
    public ResponseEntity<Map<String, Object>> getTeacherDashboard() {
        try {
            Map<String, Object> data = new HashMap<>();
            
            // Get counts
            data.put("classes", classRepository.count());
            data.put("teachers", teacherRepository.count());
            data.put("students", studentRepository.count());
            data.put("quizzes", quizRepository.count());
            
            // Get attendance statistics
            long presentCount = attendanceRepository.countByStatus("PRESENT");
            long totalAttendance = attendanceRepository.count();
            double attendancePercentage = totalAttendance > 0 ? 
                (presentCount * 100.0 / totalAttendance) : 0.0;
            data.put("attendancePercentage", Math.round(attendancePercentage * 100.0) / 100.0);
            
            return ResponseEntity.ok(data);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/hod/dashboard")
    public ResponseEntity<Map<String, Object>> getHODDashboard() {
        try {
            Map<String, Object> data = new HashMap<>();
            
            // Get counts
            data.put("classes", classRepository.count());
            data.put("teachers", teacherRepository.count());
            data.put("students", studentRepository.count());
            data.put("quizzes", quizRepository.count());
            
            // Get attendance statistics
            long presentCount = attendanceRepository.countByStatus("PRESENT");
            long totalAttendance = attendanceRepository.count();
            double attendancePercentage = totalAttendance > 0 ? 
                (presentCount * 100.0 / totalAttendance) : 0.0;
            data.put("attendancePercentage", Math.round(attendancePercentage * 100.0) / 100.0);
            
            return ResponseEntity.ok(data);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
