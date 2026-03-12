package com.smarttutor.controller;

import com.smarttutor.entity.Quiz;
import com.smarttutor.entity.Question;
import com.smarttutor.entity.QuizAttempt;
import com.smarttutor.service.QuizService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/quiz")
@CrossOrigin(origins = "*", maxAge = 3600)
public class QuizController {
    
    @Autowired
    private QuizService quizService;
    
    @PostMapping("/create")
    public ResponseEntity<Quiz> createQuiz(@Valid @RequestBody Quiz quiz) {
        try {
            Quiz createdQuiz = quizService.createQuiz(quiz);
            return ResponseEntity.ok(createdQuiz);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping
    public ResponseEntity<List<Quiz>> getAllQuizzes() {
        List<Quiz> quizzes = quizService.getAllQuizzes();
        return ResponseEntity.ok(quizzes);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Quiz> getQuizById(@PathVariable Long id) {
        try {
            Quiz quiz = quizService.getQuizById(id);
            return ResponseEntity.ok(quiz);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/{id}/questions")
    public ResponseEntity<List<Question>> getQuizQuestions(@PathVariable Long id) {
        try {
            List<Question> questions = quizService.getQuizQuestions(id);
            return ResponseEntity.ok(questions);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Quiz> updateQuiz(@PathVariable Long id, @Valid @RequestBody Quiz quiz) {
        try {
            Quiz updatedQuiz = quizService.updateQuiz(id, quiz);
            return ResponseEntity.ok(updatedQuiz);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteQuiz(@PathVariable Long id) {
        try {
            quizService.deleteQuiz(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping("/attempt/{quizId}")
    public ResponseEntity<QuizAttempt> attemptQuiz(@PathVariable Long quizId, @RequestBody Map<String, Object> attemptData) {
        try {
            Long studentId = Long.valueOf(attemptData.get("studentId").toString());
            QuizAttempt attempt = quizService.attemptQuiz(quizId, studentId);
            return ResponseEntity.ok(attempt);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/submit/{quizId}")
    public ResponseEntity<QuizAttempt> submitQuiz(@PathVariable Long quizId, @RequestBody Map<String, Object> submissionData) {
        try {
            Long studentId = Long.valueOf(submissionData.get("studentId").toString());
            @SuppressWarnings("unchecked")
            Map<String, String> answers = (Map<String, String>) submissionData.get("answers");
            QuizAttempt result = quizService.submitQuiz(quizId, studentId, answers);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/attempts/student/{studentId}")
    public ResponseEntity<List<QuizAttempt>> getStudentQuizAttempts(@PathVariable Long studentId) {
        List<QuizAttempt> attempts = quizService.getStudentQuizAttempts(studentId);
        return ResponseEntity.ok(attempts);
    }
    
    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<Quiz>> getQuizzesByTeacher(@PathVariable Long teacherId) {
        List<Quiz> quizzes = quizService.getQuizzesByTeacher(teacherId);
        return ResponseEntity.ok(quizzes);
    }
    
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Quiz>> getQuizzesForStudent(@PathVariable Long studentId) {
        List<Quiz> quizzes = quizService.getQuizzesForStudent(studentId);
        return ResponseEntity.ok(quizzes);
    }
}
