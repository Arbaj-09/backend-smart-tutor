package com.smarttutor.repository;

import com.smarttutor.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Optional<Teacher> findByEmail(String email);
    boolean existsByEmail(String email);
    
    @Query("SELECT t FROM Teacher t WHERE t.classEntity.id = :classId AND t.division.id = :divisionId")
    List<Teacher> findByClassAndDivision(@Param("classId") Long classId, @Param("divisionId") Long divisionId);
    
    @Query("SELECT t FROM Teacher t WHERE t.classEntity.id = :classId AND t.division.id = :divisionId AND t.active = true")
    Optional<Teacher> findActiveTeacherByClassAndDivision(@Param("classId") Long classId, @Param("divisionId") Long divisionId);
    
    List<Teacher> findByClassEntityId(Long classId);
    List<Teacher> findByDivisionId(Long divisionId);
    List<Teacher> findByActive(Boolean active);
}
