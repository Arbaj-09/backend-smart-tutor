package com.smarttutor.dto;

import com.smarttutor.enums.DifficultyLevel;

import java.time.LocalDateTime;
import java.util.List;

public class QuizResultDTO {
    
    private Long id;
    private String title;
    private DifficultyLevel difficulty;
    private Integer timeLimit;
    private Integer score;
    private Integer totalMarks;
    private Double percentage;
    private LocalDateTime attemptedAt;
    private List<QuestionResultDTO> questionResults;
    
    public QuizResultDTO() {}
    
    public QuizResultDTO(Long id, String title, DifficultyLevel difficulty, Integer score, Integer totalMarks, LocalDateTime attemptedAt) {
        this.id = id;
        this.title = title;
        this.difficulty = difficulty;
        this.score = score;
        this.totalMarks = totalMarks;
        this.attemptedAt = attemptedAt;
        this.percentage = totalMarks > 0 ? (double) score / totalMarks * 100 : 0.0;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public DifficultyLevel getDifficulty() {
        return difficulty;
    }
    
    public void setDifficulty(DifficultyLevel difficulty) {
        this.difficulty = difficulty;
    }
    
    public Integer getTimeLimit() {
        return timeLimit;
    }
    
    public void setTimeLimit(Integer timeLimit) {
        this.timeLimit = timeLimit;
    }
    
    public Integer getScore() {
        return score;
    }
    
    public void setScore(Integer score) {
        this.score = score;
    }
    
    public Integer getTotalMarks() {
        return totalMarks;
    }
    
    public void setTotalMarks(Integer totalMarks) {
        this.totalMarks = totalMarks;
    }
    
    public Double getPercentage() {
        return percentage;
    }
    
    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }
    
    public LocalDateTime getAttemptedAt() {
        return attemptedAt;
    }
    
    public void setAttemptedAt(LocalDateTime attemptedAt) {
        this.attemptedAt = attemptedAt;
    }
    
    public List<QuestionResultDTO> getQuestionResults() {
        return questionResults;
    }
    
    public void setQuestionResults(List<QuestionResultDTO> questionResults) {
        this.questionResults = questionResults;
    }
    
    public static class QuestionResultDTO {
        private Long questionId;
        private String question;
        private String selectedOption;
        private String correctOption;
        private Boolean isCorrect;
        
        public QuestionResultDTO() {}
        
        public QuestionResultDTO(Long questionId, String question, String selectedOption, String correctOption, Boolean isCorrect) {
            this.questionId = questionId;
            this.question = question;
            this.selectedOption = selectedOption;
            this.correctOption = correctOption;
            this.isCorrect = isCorrect;
        }
        
        public Long getQuestionId() {
            return questionId;
        }
        
        public void setQuestionId(Long questionId) {
            this.questionId = questionId;
        }
        
        public String getQuestion() {
            return question;
        }
        
        public void setQuestion(String question) {
            this.question = question;
        }
        
        public String getSelectedOption() {
            return selectedOption;
        }
        
        public void setSelectedOption(String selectedOption) {
            this.selectedOption = selectedOption;
        }
        
        public String getCorrectOption() {
            return correctOption;
        }
        
        public void setCorrectOption(String correctOption) {
            this.correctOption = correctOption;
        }
        
        public Boolean getIsCorrect() {
            return isCorrect;
        }
        
        public void setIsCorrect(Boolean isCorrect) {
            this.isCorrect = isCorrect;
        }
    }
}
