package com.smarttutor.service;

import com.smarttutor.entity.Attendance;
import com.smarttutor.entity.Student;
import com.smarttutor.entity.Teacher;
import com.smarttutor.enums.AttendanceStatus;
import com.smarttutor.repository.AttendanceRepository;
import com.smarttutor.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AttendanceService {
    
    @Autowired
    private AttendanceRepository attendanceRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    public Attendance markAttendance(Attendance attendance) {
        // Check if attendance already exists for this student and date
        if (attendanceRepository.findByStudentAndDate(
                attendance.getStudent().getId(), 
                attendance.getAttendanceDate()).isPresent()) {
            throw new RuntimeException("Attendance already marked for this student on this date");
        }
        
        return attendanceRepository.save(attendance);
    }
    
    public List<Attendance> getAllAttendance() {
        return attendanceRepository.findAll();
    }
    
    public List<Attendance> getAttendanceByDate(LocalDate date) {
        return attendanceRepository.findByAttendanceDate(date);
    }
    
    public List<Attendance> getAttendanceByStudent(Long studentId) {
        return attendanceRepository.findByStudentId(studentId);
    }
    
    public List<Attendance> getAttendanceByTeacher(Long teacherId) {
        return attendanceRepository.findByTeacherId(teacherId);
    }
    
    public Double getAttendancePercentage(Long studentId) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(30);
        
        Long totalDays = attendanceRepository.countByStudentAndStatusAndDateRange(
                studentId, AttendanceStatus.PRESENT, startDate, endDate);
        Long presentDays = attendanceRepository.countByStudentAndStatusAndDateRange(
                studentId, AttendanceStatus.PRESENT, startDate, endDate);
        
        if (totalDays == 0) return 0.0;
        return (double) presentDays / totalDays * 100;
    }
}
