package com.smarttutor.service;

import com.smarttutor.dto.DashboardStatsDTO;
import com.smarttutor.entity.Hod;
import com.smarttutor.exception.ResourceNotFoundException;
import com.smarttutor.repository.HodRepository;
import com.smarttutor.repository.ClassRepository;
import com.smarttutor.repository.TeacherRepository;
import com.smarttutor.repository.StudentRepository;
import com.smarttutor.repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class HodService {
    
    @Autowired
    private HodRepository hodRepository;
    
    @Autowired
    private ClassRepository classRepository;
    
    @Autowired
    private TeacherRepository teacherRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private AttendanceRepository attendanceRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public Hod createHod(Hod hod) {
        if (hodRepository.existsByEmail(hod.getEmail())) {
            throw new IllegalArgumentException("HOD with email " + hod.getEmail() + " already exists");
        }
        
        // Hash password
        hod.setPassword(passwordEncoder.encode(hod.getPassword()));
        
        return hodRepository.save(hod);
    }
    
    public Hod getHodById(Long id) {
        return hodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("HOD", "id", id));
    }
    
    public List<Hod> getAllHods() {
        return hodRepository.findAll();
    }
    
    public DashboardStatsDTO getHODDashboardStats() {
        DashboardStatsDTO stats = new DashboardStatsDTO();
        
        // Total counts
        stats.setTotalClasses(classRepository.count());
        stats.setTotalTeachers(teacherRepository.count());
        stats.setTotalStudents(studentRepository.count());
        
        // Overall attendance percentage (last 30 days)
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(30);
        Double overallAttendance = attendanceRepository.getOverallAttendancePercentage(startDate, endDate);
        stats.setOverallAttendancePercentage(overallAttendance != null ? overallAttendance : 0.0);
        
        return stats;
    }

    public Hod updateHod(Long id, Hod hod) {
        Hod existingHod = getHodById(id);
        
        existingHod.setName(hod.getName());
        existingHod.setEmail(hod.getEmail());
        existingHod.setActive(hod.getActive());
        existingHod.setFcmToken(hod.getFcmToken());
        
        return hodRepository.save(existingHod);
    }
}
