package com.smarttutor.controller;

import com.smarttutor.entity.Attendance;
import com.smarttutor.enums.AttendanceStatus;
import com.smarttutor.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AttendanceController {
    
    @Autowired
    private AttendanceService attendanceService;
    
    @PostMapping("/mark")
    public ResponseEntity<Attendance> markAttendance(@RequestBody Attendance attendance) {
        try {
            Attendance markedAttendance = attendanceService.markAttendance(attendance);
            return ResponseEntity.ok(markedAttendance);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping
    public ResponseEntity<List<Attendance>> getAllAttendance() {
        List<Attendance> attendances = attendanceService.getAllAttendance();
        return ResponseEntity.ok(attendances);
    }
    
    @GetMapping("/date/{date}")
    public ResponseEntity<List<Attendance>> getAttendanceByDate(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        List<Attendance> attendances = attendanceService.getAttendanceByDate(date);
        return ResponseEntity.ok(attendances);
    }
    
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Attendance>> getAttendanceByStudent(@PathVariable Long studentId) {
        List<Attendance> attendances = attendanceService.getAttendanceByStudent(studentId);
        return ResponseEntity.ok(attendances);
    }
    
    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<Attendance>> getAttendanceByTeacher(@PathVariable Long teacherId) {
        List<Attendance> attendances = attendanceService.getAttendanceByTeacher(teacherId);
        return ResponseEntity.ok(attendances);
    }
    
    @GetMapping("/student/{studentId}/percentage")
    public ResponseEntity<Double> getAttendancePercentage(@PathVariable Long studentId) {
        try {
            Double percentage = attendanceService.getAttendancePercentage(studentId);
            return ResponseEntity.ok(percentage);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
