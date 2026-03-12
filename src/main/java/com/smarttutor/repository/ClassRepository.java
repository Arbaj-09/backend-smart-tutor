package com.smarttutor.repository;

import com.smarttutor.entity.ClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassRepository extends JpaRepository<ClassEntity, Long> {
    Optional<ClassEntity> findByClassName(String className);
    boolean existsByClassName(String className);
    
    // Get student and teacher counts for a class
    @Query("SELECT COUNT(s) FROM Student s JOIN s.division d WHERE d.classEntity.id = :classId")
    Integer countStudentsByClassId(@Param("classId") Long classId);
    
    @Query("SELECT COUNT(DISTINCT tcd.teacher.id) FROM TeacherClassDivision tcd WHERE tcd.division.classEntity.id = :classId")
    Integer countTeachersByClassId(@Param("classId") Long classId);
}
