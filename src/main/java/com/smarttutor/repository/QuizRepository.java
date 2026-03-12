package com.smarttutor.repository;

import com.smarttutor.entity.Quiz;
import com.smarttutor.enums.DifficultyLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
    
    @Query("SELECT q FROM Quiz q WHERE q.classEntity.id = :classId AND q.division.id = :divisionId")
    List<Quiz> findByClassAndDivision(@Param("classId") Long classId, @Param("divisionId") Long divisionId);
    
    @Query("SELECT q FROM Quiz q WHERE q.teacher.id = :teacherId")
    List<Quiz> findByTeacherId(@Param("teacherId") Long teacherId);
    
    @Query("SELECT q FROM Quiz q WHERE q.classEntity.id = :classId AND q.division.id = :divisionId")
    List<Quiz> findByClassIdAndDivisionId(@Param("classId") Long classId, @Param("divisionId") Long divisionId);
    
    @Query("SELECT q FROM Quiz q WHERE q.difficulty = :difficulty")
    List<Quiz> findByDifficulty(@Param("difficulty") DifficultyLevel difficulty);
    
    @Query("SELECT q FROM Quiz q WHERE q.classEntity.id = :classId AND q.division.id = :divisionId AND q.difficulty = :difficulty")
    List<Quiz> findByClassDivisionAndDifficulty(@Param("classId") Long classId, @Param("divisionId") Long divisionId, @Param("difficulty") DifficultyLevel difficulty);
    
    List<Quiz> findByClassEntityId(Long classId);
    List<Quiz> findByDivisionId(Long divisionId);
}
