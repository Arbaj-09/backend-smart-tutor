package com.smarttutor.repository;

import com.smarttutor.entity.QuizAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Long> {
    
    @Query("SELECT qa FROM QuizAttempt qa WHERE qa.quiz.id = :quizId AND qa.student.id = :studentId")
    Optional<QuizAttempt> findByQuizAndStudent(@Param("quizId") Long quizId, @Param("studentId") Long studentId);
    
    @Query("SELECT qa FROM QuizAttempt qa WHERE qa.student.id = :studentId")
    List<QuizAttempt> findByStudentId(@Param("studentId") Long studentId);
    
    @Query("SELECT qa FROM QuizAttempt qa WHERE qa.quiz.id = :quizId")
    List<QuizAttempt> findByQuizId(@Param("quizId") Long quizId);
    
    @Query("SELECT qa FROM QuizAttempt qa WHERE qa.quiz.teacher.id = :teacherId")
    List<QuizAttempt> findByTeacherId(@Param("teacherId") Long teacherId);
    
    @Query("SELECT AVG(CAST(qa.score AS double) / qa.totalMarks * 100) FROM QuizAttempt qa WHERE qa.quiz.id = :quizId")
    Double getAverageScoreByQuiz(@Param("quizId") Long quizId);
    
    @Query("SELECT AVG(CAST(qa.score AS double) / qa.totalMarks * 100) FROM QuizAttempt qa WHERE qa.student.id = :studentId")
    Double getAverageScoreByStudent(@Param("studentId") Long studentId);
}
