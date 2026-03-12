package com.smarttutor.service;

import com.smarttutor.entity.Quiz;
import com.smarttutor.entity.Question;
import com.smarttutor.entity.QuizAttempt;
import com.smarttutor.entity.Student;
import com.smarttutor.repository.QuizRepository;
import com.smarttutor.repository.QuestionRepository;
import com.smarttutor.repository.QuizAttemptRepository;
import com.smarttutor.repository.StudentRepository;
import com.smarttutor.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class QuizService {
    
    @Autowired
    private QuizRepository quizRepository;
    
    @Autowired
    private QuestionRepository questionRepository;
    
    @Autowired
    private QuizAttemptRepository quizAttemptRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    public Quiz createQuiz(Quiz quiz) {
        return quizRepository.save(quiz);
    }
    
    public List<Quiz> getAllQuizzes() {
        return quizRepository.findAll();
    }
    
    public Quiz getQuizById(Long id) {
        return quizRepository.findById(id).orElse(null);
    }
    
    public List<Question> getQuizQuestions(Long quizId) {
        return questionRepository.findByQuizId(quizId);
    }
    
    public Quiz updateQuiz(Long id, Quiz quiz) {
        Quiz existingQuiz = getQuizById(id);
        if (existingQuiz == null) {
            throw new RuntimeException("Quiz not found");
        }
        
        existingQuiz.setTitle(quiz.getTitle());
        existingQuiz.setDifficulty(quiz.getDifficulty());
        existingQuiz.setTimeLimit(quiz.getTimeLimit());
        
        return quizRepository.save(existingQuiz);
    }
    
    public void deleteQuiz(Long id) {
        quizRepository.deleteById(id);
    }
    
    public QuizAttempt attemptQuiz(Long quizId, Long studentId) {
        Quiz quiz = getQuizById(quizId);
        Student student = studentRepository.findById(studentId).orElse(null);
        
        if (quiz == null || student == null) {
            throw new RuntimeException("Quiz or Student not found");
        }
        
        QuizAttempt attempt = new QuizAttempt();
        attempt.setQuiz(quiz);
        attempt.setStudent(student);
        attempt.setScore(0);
        attempt.setTotalMarks(0);
        
        return quizAttemptRepository.save(attempt);
    }
    
    public QuizAttempt submitQuiz(Long quizId, Long studentId, Map<String, String> answers) {
        Quiz quiz = getQuizById(quizId);
        List<Question> questions = getQuizQuestions(quizId);
        
        int score = 0;
        int totalMarks = questions.size();
        
        for (Question question : questions) {
            String correctAnswer = question.getCorrectOption();
            String userAnswer = answers.get("q" + question.getId());
            
            if (correctAnswer.equals(userAnswer)) {
                score++;
            }
        }
        
        QuizAttempt attempt = new QuizAttempt();
        attempt.setQuiz(quiz);
        attempt.setStudent(studentRepository.findById(studentId).orElse(null));
        attempt.setScore(score);
        attempt.setTotalMarks(totalMarks);
        
        return quizAttemptRepository.save(attempt);
    }
    
    public List<QuizAttempt> getStudentQuizAttempts(Long studentId) {
        return quizAttemptRepository.findByStudentId(studentId);
    }
    
    public List<Quiz> getQuizzesByTeacher(Long teacherId) {
        return quizRepository.findByTeacherId(teacherId);
    }
    
    public List<Quiz> getQuizzesForStudent(Long studentId) {
        Student student = studentRepository.findById(studentId).orElse(null);
        if (student == null) return new ArrayList<>();
        
        return quizRepository.findByClassIdAndDivisionId(student.getClassEntity().getId(), student.getDivision().getId());
    }
}
