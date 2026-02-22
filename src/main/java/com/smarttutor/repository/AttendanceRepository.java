package com.smarttutor.repository;

import com.smarttutor.entity.Attendance;
import com.smarttutor.enums.AttendanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    
    @Query("SELECT a FROM Attendance a WHERE a.student.id = :studentId AND a.attendanceDate = :date")
    Optional<Attendance> findByStudentAndDate(@Param("studentId") Long studentId, @Param("date") LocalDate date);
    
    @Query("SELECT a FROM Attendance a WHERE a.teacher.id = :teacherId AND a.attendanceDate = :date")
    List<Attendance> findByTeacherAndDate(@Param("teacherId") Long teacherId, @Param("date") LocalDate date);
    
    @Query("SELECT a FROM Attendance a WHERE a.student.id = :studentId AND a.attendanceDate BETWEEN :startDate AND :endDate")
    List<Attendance> findByStudentAndDateRange(@Param("studentId") Long studentId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT a FROM Attendance a WHERE a.teacher.id = :teacherId AND a.student.classEntity.id = :classId AND a.student.division.id = :divisionId AND a.attendanceDate = :date")
    List<Attendance> findByTeacherClassDivisionAndDate(@Param("teacherId") Long teacherId, @Param("classId") Long classId, @Param("divisionId") Long divisionId, @Param("date") LocalDate date);
    
    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.student.id = :studentId AND a.status = :status AND a.attendanceDate BETWEEN :startDate AND :endDate")
    Long countByStudentAndStatusAndDateRange(@Param("studentId") Long studentId, @Param("status") AttendanceStatus status, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    List<Attendance> findByStudentId(Long studentId);
    List<Attendance> findByTeacherId(Long teacherId);
    List<Attendance> findByAttendanceDate(LocalDate date);
    
    @Query("SELECT COALESCE(SUM(CASE WHEN a.status = 'PRESENT' THEN 1 ELSE 0 END) * 100.0 / COUNT(a), 0) " +
           "FROM Attendance a WHERE a.attendanceDate BETWEEN :startDate AND :endDate")
    Double getOverallAttendancePercentage(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
