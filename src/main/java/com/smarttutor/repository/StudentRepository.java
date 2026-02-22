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
    
    @Query("SELECT s FROM Student s WHERE s.classEntity.id = :classId AND s.division.id = :divisionId")
    List<Student> findByClassAndDivision(@Param("classId") Long classId, @Param("divisionId") Long divisionId);
    
    @Query("SELECT s FROM Student s WHERE s.classEntity.id = :classId AND s.division.id = :divisionId AND s.active = true")
    List<Student> findActiveStudentsByClassAndDivision(@Param("classId") Long classId, @Param("divisionId") Long divisionId);
    
    List<Student> findByClassEntityId(Long classId);
    List<Student> findByDivisionId(Long divisionId);
    List<Student> findByTeacherId(Long teacherId);
    List<Student> findByActive(Boolean active);
    
    Optional<Student> findByRollNoAndClassEntityIdAndDivisionId(String rollNo, Long classId, Long divisionId);
}
