package com.smarttutor.dto.response;

import java.time.LocalDateTime;

public class DivisionResponseDTO {
    private Long id;
    private String divisionName;
    private Long classId;
    private String className;
    private LocalDateTime createdAt;
    private Integer studentCount;

    // Constructors
    public DivisionResponseDTO() {}

    public DivisionResponseDTO(Long id, String divisionName, Long classId, String className, LocalDateTime createdAt) {
        this.id = id;
        this.divisionName = divisionName;
        this.classId = classId;
        this.className = className;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDivisionName() { return divisionName; }
    public void setDivisionName(String divisionName) { this.divisionName = divisionName; }

    public Long getClassId() { return classId; }
    public void setClassId(Long classId) { this.classId = classId; }

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public Integer getStudentCount() { return studentCount; }
    public void setStudentCount(Integer studentCount) { this.studentCount = studentCount; }
}
