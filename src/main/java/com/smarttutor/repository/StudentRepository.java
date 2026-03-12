package com.smarttutor.repository;

import com.smarttutor.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByEmail(String email);
    boolean existsByEmail(String email);
    
    // Eager loading queries to prevent lazy loading issues
    @Query("SELECT DISTINCT s FROM Student s LEFT JOIN FETCH s.classEntity LEFT JOIN FETCH s.division LEFT JOIN FETCH s.teacher")
    List<Student> findAllWithEagerLoading();
    
    @Query("SELECT s FROM Student s LEFT JOIN FETCH s.classEntity LEFT JOIN FETCH s.division LEFT JOIN FETCH s.teacher WHERE s.id = :id")
    Optional<Student> findByIdWithEagerLoading(@Param("id") Long id);
    
    @Query("SELECT s FROM Student s WHERE s.classEntity.id = :classId AND s.division.id = :divisionId")
    List<Student> findByClassAndDivision(@Param("classId") Long classId, @Param("divisionId") Long divisionId);
    
    @Query("SELECT s FROM Student s WHERE s.classEntity.id = :classId AND s.division.id = :divisionId AND s.active = true")
    List<Student> findActiveStudentsByClassAndDivision(@Param("classId") Long classId, @Param("divisionId") Long divisionId);
    
    @Query("SELECT DISTINCT s FROM Student s LEFT JOIN FETCH s.classEntity LEFT JOIN FETCH s.division LEFT JOIN FETCH s.teacher WHERE s.teacher.id = :teacherId")
    List<Student> findByTeacherId(@Param("teacherId") Long teacherId);
    
    @Query("SELECT DISTINCT s FROM Student s LEFT JOIN FETCH s.classEntity LEFT JOIN FETCH s.division LEFT JOIN FETCH s.teacher WHERE s.classEntity.id = :classId")
    List<Student> findByClassEntityId(@Param("classId") Long classId);
    
    @Query("SELECT DISTINCT s FROM Student s LEFT JOIN FETCH s.classEntity LEFT JOIN FETCH s.division LEFT JOIN FETCH s.teacher WHERE s.classEntity.id = :classId AND s.division.id = :divisionId")
    List<Student> findByClassEntityIdAndDivisionId(@Param("classId") Long classId, @Param("divisionId") Long divisionId);
    
    List<Student> findByDivisionId(Long divisionId);
    List<Student> findByActive(Boolean active);
    
    Optional<Student> findByRollNoAndClassEntityIdAndDivisionId(String rollNo, Long classId, Long divisionId);
}
