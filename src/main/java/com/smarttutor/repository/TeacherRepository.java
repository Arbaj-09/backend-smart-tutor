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
    
    @Query("SELECT t FROM Teacher t LEFT JOIN FETCH t.assignments a LEFT JOIN FETCH a.classEntity LEFT JOIN FETCH a.division")
    List<Teacher> findAllWithAssignments();
    
    @Query("SELECT t FROM Teacher t LEFT JOIN FETCH t.assignments a LEFT JOIN FETCH a.classEntity LEFT JOIN FETCH a.division WHERE t.id = :id")
    Optional<Teacher> findByIdWithAssignments(@Param("id") Long id);
    
    List<Teacher> findByActive(Boolean active);
}
