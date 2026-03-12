package com.smarttutor.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class ClassResponseDTO {
    private Long id;
    private String className;
    private LocalDateTime createdAt;
    private List<DivisionResponseDTO> divisions;
    private Integer studentCount;
    private Integer teacherCount;

    // Constructors
    public ClassResponseDTO() {}

    public ClassResponseDTO(Long id, String className, LocalDateTime createdAt) {
        this.id = id;
        this.className = className;
        this.createdAt = createdAt;
        this.studentCount = 0;
        this.teacherCount = 0;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<DivisionResponseDTO> getDivisions() { return divisions; }
    public void setDivisions(List<DivisionResponseDTO> divisions) { this.divisions = divisions; }
    
    public Integer getStudentCount() { return studentCount; }
    public void setStudentCount(Integer studentCount) { this.studentCount = studentCount; }
    
    public Integer getTeacherCount() { return teacherCount; }
    public void setTeacherCount(Integer teacherCount) { this.teacherCount = teacherCount; }
}
